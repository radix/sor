; PERLIN CODE
; github.com/indy/perlin

; Copyright (c) 2010 Inderjit Gill

; Released under an MIT license.

; Permission is hereby granted, free of charge, to any person
; obtaining a copy of this software and associated documentation
; files (the "Software"), to deal in the Software without
; restriction, including without limitation the rights to use,
; copy, modify, merge, publish, distribute, sublicense, and/or sell
; copies of the Software, and to permit persons to whom the
; Software is furnished to do so, subject to the following
; conditions:

; The above copyright notice and this permission notice shall be
; included in all copies or substantial portions of the Software.

; THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
; EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
; OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
; NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
; HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
; WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
; FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
; OTHER DEALINGS IN THE SOFTWARE.


(ns perlin.core)

(def p [151 160 137 91 90 15 131 13 201 95 96 53 194 233 7 225
        140 36 103 30 69 142 8 99 37 240 21 10 23 190  6 148
        247 120 234 75 0 26 197 62 94 252 219 203 117 35 11 32
        57 177 33 88 237 149 56 87 174 20 125 136 171 168  68 175
        74 165 71 134 139 48 27 166 77 146 158 231 83 111 229 122
        60 211 133 230 220 105 92 41 55 46 245 40 244 102 143 54
        65 25 63 161  1 216 80 73 209 76 132 187 208  89 18 169
        200 196 135 130 116 188 159 86 164 100 109 198 173 186  3 64
        52 217 226 250 124 123 5 202 38 147 118 126 255 82 85 212
        207 206 59 227 47 16 58 17 182 189 28 42 223 183 170 213
        119 248 152  2 44 154 163  70 221 153 101 155 167  43 172 9
        129 22 39 253  19 98 108 110 79 113 224 232 178 185  112 104
        218 246 97 228 251 34 242 193 238 210 144 12 191 179 162 241
        81 51 145 235 249 14 239 107 49 192 214  31 181 199 106 157
        184  84 204 176 115 121 50 45 127  4 150 254 138 236 205 93
        222 114 67 29 24 72 243 141 128 195 78 66 215 61 156 180
        151 160 137 91 90 15 131 13 201 95 96 53 194 233 7 225
        140 36 103 30 69 142 8 99 37 240 21 10 23 190  6 148
        247 120 234 75 0 26 197 62 94 252 219 203 117 35 11 32
        57 177 33 88 237 149 56 87 174 20 125 136 171 168  68 175
        74 165 71 134 139 48 27 166 77 146 158 231 83 111 229 122
        60 211 133 230 220 105 92 41 55 46 245 40 244 102 143 54
        65 25 63 161  1 216 80 73 209 76 132 187 208  89 18 169
        200 196 135 130 116 188 159 86 164 100 109 198 173 186  3 64
        52 217 226 250 124 123 5 202 38 147 118 126 255 82 85 212
        207 206 59 227 47 16 58 17 182 189 28 42 223 183 170 213
        119 248 152  2 44 154 163  70 221 153 101 155 167  43 172 9
        129 22 39 253  19 98 108 110 79 113 224 232 178 185  112 104
        218 246 97 228 251 34 242 193 238 210 144 12 191 179 162 241
        81 51 145 235 249 14 239 107 49 192 214  31 181 199 106 157
        184  84 204 176 115 121 50 45 127  4 150 254 138 236 205 93
        222 114 67 29 24 72 243 141 128 195 78 66 215 61 156 180])

(defn fade
  [t]
  (* t t t (+ (* t (- (* t 6) 15.0)) 10.0)))

(defn lerp
  [t a b]
  (+ a (* t (- b a))))

(defn grad
  [hash x y z]
  (let [h (bit-and hash 15)
        u (if (< h 8) x y)
        v (if (< h 4) y (if (or (= h 12) (= h 14)) x z))]
    (+ (if (= (bit-and h 1) 0) u (- u))
       (if (= (bit-and h 2) 0) v (- v)))))

(defn floor [x] (.floor js/Math x)) ; added by radix

(defn noise
  [x y z]
  (let [X (bit-and (floor x) 255)
        Y (bit-and (floor y) 255)
        Z (bit-and (floor z) 255)
        xx (- x (floor x))
        yy (- y (floor y))
        zz (- z (floor z))
        u (fade xx)
        v (fade yy)
        w (fade zz)
        A (+ (p X) Y)
        AA (+ (p A) Z)
        AB (+ (p (+ A 1)) Z)
        B (+ (p (+ X 1)) Y)
        BA (+ (p B) Z)
        BB (+ (p (+ B 1)) Z)]
    (lerp w
          (lerp v
                (lerp u
                      (grad (p AA) xx yy zz)
                      (grad (p BA) (- xx 1) yy zz))
                (lerp u
                      (grad (p AB) xx (- yy 1) zz)
                      (grad (p BB) (- xx 1) (- yy 1) zz)))
          (lerp v
                (lerp u
                      (grad (p (+ AA 1)) xx yy (- zz 1))
                      (grad (p (+ BA 1)) (- xx 1) yy (- zz 1)))
                (lerp u
                      (grad (p (+ AB 1)) xx (- yy 1) (- zz 1))
                      (grad (p (+ BB 1)) (- xx 1) (- yy 1) (- zz 1)))))))

; END PERLIN CODE

; START SOR CODE
(ns sor.core
  (:use [perlin.core :only [noise]]))

(defn noise-grid [height width step]
  (for [x (range 0 width step)]
    (for [y (range 0 height step)]
      (noise x y 0))))

(noise-grid 0.4 0.4 0.1)

