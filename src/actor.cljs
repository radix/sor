(ns sor.actor)

(def tracer (atom []))
tracer
(defn debug [prefix stuff]
  (swap! tracer conj {:event prefix :data stuff})
  stuff)

(defn get-frames
  "Return the frames in a frameset, given the :sprite and :frameset."
  [sprite frameset]
  (((framesets sprite) frameset) :frames))

; this is probably *horribly* inefficient, I'm recalculating tons of stuff.
; optimization: instead of incrementally summing the duration of the frames to determine the current one,
; rewrite the frame duration datastructure to be based on the number of ms past the start of the loop that the frame should be played.
; simply put, instead of [2 2 1 2], a framelist should be [2 4 5 7].
; one problem to figure out with this is how long the final frame should last.
; warning, this breaks if you have a frame duration of 0.

; also, if we end up having consistent frame times for all animations, this can be drastically optimized to little more than a divmod call.
; (hint: let's do that)

(defn get-next-frame
  "Calculate the next frame to draw based on the amount of time that has passed since the start of the animation.
  frames is a seq of durations.
  elapsed is the time since the last simulation update.
  Returns a mapping:
    :elapsed -> new time since start of animation loop
    :frame -> frame index to draw right now."
  [frames elapsed]
  (let [new-elapsed (mod elapsed (apply + frames))
        find-frame (fn [frames-elapsed frames i]
                     (if (< new-elapsed (apply + (vec (take (+ i 1) frames))))
                       i
                       (recur (+ frames-elapsed (frames i)) frames (+ i 1))))]
    {:elapsed new-elapsed
     :frame (find-frame 0 frames 0)}))

(defn animation-step
  "Return a new actor animation value (suitable for the :animation key in an actor)
  given the previous animation value and the time since the last simulation update."
  [animation-state elapsed]
  (let [frames (get-frames (animation-state :sprite) (animation-state :frameset))]
    (conj animation-state (get-next-frame (vec (map :duration frames)) elapsed))))

;; some test data
(def monster
  {
    :id 1
    :x 0, :y 0
    :width 100, :height 100
    :actor {
      :state :idle, ; :aggressive, :attacking, :blocking, :dodging, :invincible
      ; additional fields may become available in different states:
      ; :target <actor-id>
      ; :buffs and :debuffs?
      ; :interrupts? {:attack {:source <id> :weapon {...}}}
    }
    :animation {
      :sprite :rabite
      :frameset :breathing-left
      :frame 0 ; which frame of the animation the actor is on
      :elapsed 0 ; the time elapsed since the animation started.
    }
  })

;; some more test data
(def framesets
  {
    :rabite {
      :breathing-left {
        :spritesheet "rabite.png"
        :frames [{:x 0 :y 0 :height 128 :width 128 :duration 10}
                 {:x 128 :y 0 :height 128 :width 128 :duration 10}]}
      :attacking-left {
        :spritesheet "rabite.png"
        :frames [{:x 0 :y 128 :height 128 :width 128 :duration 10}
                 {:x 128 :y 128 :height 128 :width 128 :duration 10}]}}})


(get-frames :rabite :breathing-left)

(let [anim (monster :animation)]
  (get-frames (anim :sprite) (anim :frameset)))

(get-next-frame [2 2 2 2] 4) ;{:elapsed 4 :frame 2}
(get-next-frame [1] 10) ;{:elapsed 0 :frame 0}

(get-next-frame [10] 0) ; 0
(get-next-frame [10] 10) ; 0
(get-next-frame [10 10] 10) ; 1
(get-next-frame [10 10 10] 20) ; 2
(get-next-frame [10 10] 20) ; 0

(animation-step (monster :animation) 9)
(animation-step (monster :animation) 10)
(animation-step (monster :animation) 19)
(animation-step (monster :animation) 20)

#_"
Consider:

#interrupts
Something a system function can place in actor-state to get the
actor-state updating function to consider some input.

allowed interrupts...
 :idle
   pretty much anything
 :blocking
   :attack, but damage will be reduced
 :invincible
   nothing.

alternatively, invert it (are there any interrupts other than 'attack'?... 'balloon'? 'item'?)
attack -> valid states are :idle, :blocking, but not :dodging or :invincible.
spell -> maybe any state is valid, if spells are expensive and we don't want
         to block them with bad timing.


#collisions
An idle AC actor can walk into a tree and must be stopped.
An idle AC actor can walk into another AC actor. Their vectors should be summed.

"
