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

; Potential optimization for get-next-frame:

; Instead of incrementally summing the duration of the frames to determine
; the current one, rewrite the frame duration datastructure to be based on
; the number of ms past the start of the loop that the frame should be
; played. simply put, instead of [2 2 1 2], a framelist should be [2 4 5 7].
; one problem to figure out with this is how long the final frame
; should last. warning, this breaks if you have a frame duration of 0.

; also, if we end up having consistent frame times for all animations,
; this can be drastically optimized to little more than a divmod call.
; (hint: let's do that)

(defn get-next-frame
  "Calculate the next frame to draw based on the amount of time that has passed since the start of the animation.

  - frames is a seq of durations.
  - elapsed is the time since the last simulation update.

  Returns a mapping:
    :elapsed -> new time since start of animation loop
    :frame -> frame index to draw right now."
  [frames elapsed]
  (let [new-elapsed (mod elapsed (apply + frames))
        find-frame (fn [frames frames-elapsed i]
                     (if (< new-elapsed frames-elapsed)
                       (- i 1)
                       (recur frames (+ frames-elapsed (frames i)) (+ i 1))))]
    {:elapsed new-elapsed
     :frame (find-frame frames 0 0)}))


(defn get-next-regular-frame
  "Calculate the next frame to draw based on the amount of time that has passed since the start of the animation, assuming consistent frame duration.

  - num-frames is the number of frames in the loop
  - frame-length is the duration of each frame.
  - elapsed is the time since the last simulation update.

  Returns a mapping:
    :elapsed -> new time since start of animation loop
    :frame -> frame index to draw right now."
  [num-frames frame-length elapsed]
  (let [new-elapsed (mod elapsed (* num-frames frame-length))]
    {:elapsed new-elapsed
     :frame (/ new-elapsed frame-length)}))

;TODO: Support non-looping animations, I think?
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


(defn assert=
  [a b]
  (if (not= a b)
    [:bad a]
    :good))

(assert= (get-next-frame [2 2 2 2] 4)
         {:elapsed 4 :frame 2})
(assert= (get-next-frame [1] 10)
         {:elapsed 0 :frame 0})
(assert= (get-next-frame [10] 0)
         {:elapsed 0 :frame 0})
(assert= (get-next-frame [10] 10)
         {:elapsed 0 :frame 0})
(assert= (get-next-frame [10 10] 10)
         {:elapsed 10 :frame 1})
(assert= (get-next-frame [10 10 10] 20)
         {:elapsed 20 :frame 2})
(assert= (get-next-frame [10 10] 20)
         {:elapsed 0 :frame 0})
(assert= (get-next-frame [1 2 4] 4)
         {:elapsed 4 :frame 2})
(assert= (get-next-frame [1 3 4] 4)
         {:elapsed 4 :frame 2})
(assert= (get-next-frame [2 2 3] 8)
         {:elapsed 1 :frame 0})

(assert= (get-next-regular-frame 4 20 60)
         {:elapsed 60 :frame 3})
(assert= (get-next-regular-frame 1 1 532)
         {:elapsed 0 :frame 0})
(assert= (get-next-regular-frame 2 1 3)
         {:elapsed 1 :frame 1})
(assert= (get-next-regular-frame 1 10 0)
         {:elapsed 0 :frame 0})

(assert= (animation-step (monster :animation) 0)
         {:sprite :rabite :frameset :breathing-left :frame 0 :elapsed 0})
(assert= (animation-step (monster :animation) 9)
         {:sprite :rabite :frameset :breathing-left :frame 0 :elapsed 9})
(assert= (animation-step (monster :animation) 10)
         {:sprite :rabite :frameset :breathing-left :frame 1 :elapsed 10})
(assert= (animation-step (monster :animation) 11)
         {:sprite :rabite :frameset :breathing-left :frame 1 :elapsed 11})
(assert= (animation-step (monster :animation) 19)
         {:sprite :rabite :frameset :breathing-left :frame 1 :elapsed 19})
(assert= (animation-step (monster :animation) 20)
         {:sprite :rabite :frameset :breathing-left :frame 0 :elapsed 0})


#_"
Walkin' around!

The very lowest level:
 - velocity on x and y (alternatively, represent it as one vector)
 - animation state (:boy :walking-left)

Up from that, we need the AI to have a state, such as:

:idle -> :walking-left

or

:combat -> [:approaching <boy-id>] -> :walking-left


ok, let's try top-down.

be-aggressive could be a top-level goal for a monster.

be-aggressive
  near-enemy? attack-enemy
  idle

Another top-level goal (for a friendly villager) would be

idle
  random-step

random-step
  (duration 5 (walk (random)))
"


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

(some design considerations here...)
alternatively, invert it (are there any interrupts other than 'attack'?... 'balloon'? 'item'?)
attack -> valid states are :idle, :blocking, but not :dodging or :invincible.
spell -> maybe any state is valid, if spells are expensive and we don't want
         to block them with bad timing.


#collisions
An idle AC actor can walk into a tree and must be stopped.
An idle AC actor can walk into another AC actor. Their vectors should be summed.

"
