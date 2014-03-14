(ns sor.worldgen
  (:use [sor.perlin :only [noise]]))

(defn noise-grid [height width step]
  (for [x (range 0 width step)]
    (for [y (range 0 height step)]
      (noise x y 0))))

(comment
  (noise-grid 0.4 0.4 0.1)
)

