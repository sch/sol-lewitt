<p align="center">
  <img title="Wall Drawing 419" alt="Wall Drawing 419" src="https://raw.githubusercontent.com/sch/sol-lewitt/master/resources/public/img/image.jpeg" />
</p>

> The wall is bordered and divided horizontally and vertically into four equal parts with a 6-inch (15 cm) black ink band. Each quarter has alternating parallel 6-inch (15 cm) bands of white and color ink bands. Upper left: gray; upper right: yellow; lower left: red; lower right: blue.
>
> — [Wall Drawing 419](http://www.massmoca.org/lewitt/walldrawing.php?id=419)

Sol LeWitt created instructional artworks, and left the implementation to draftsmen. This project aims to create a declarative grammar for these types of artworks where the computer can do the drafting.


## Getting it running

This is your typical figwheel song and dance:

    lein figwheel
    open http://localhost:3449


## Some notes about SVG rendering

This is using rum to build of a tree of SVG elements. Right now, the `svg` component takes a `drawing` definition map, which it knows how to destructure into SVG elements. This is kind of awkward, but I'm going with it as a possible path.

Side-note: do none of these clojurescript react wrappers support component definitions that can display `children` props? This is a [super useful](https://facebook.github.io/react/docs/multiple-components.html#children) feature in react, and it's surprising not finding that functionality in any of these libs (reagent, quiescent, rum).

Oh, probable answer: that pattern is pretty uncommon with hiccup/reagent style components, because you're just building up one big vector of HTML. So rather than wanting to do something like:

```clojure
(rum/defc svg [components] [:svg nil components])
(rum/defc svg-group [shapes] [:g nil shapes])
(rum/defc svg-line [line color] [:line (assoc (line-map line) :stroke color)])

(rum/mount (svg (svg-group [(svg-line line1) (svg-line line2)])) app)
```

...which doesn't work, you should just build up a vector using functions, and render into a generic SVG component:

```clojure
(rum/defc svg [drawing] [:svg nil drawing])
(defn svg-group [shapes] [:g nil shapes])
(defn svg-line [line color] [:line (assoc (line-map line) :stroke color)])

(rum/mount (svg (svg-group [(svg-line line1) (svg-line line2)])) app)
```

The only things that need to change in that example are the intermediate components -- rendering remains the same.


## Some useful resources

- http://www.w3.org/Graphics/SVG/IG/resources/svgprimer.html
- http://svgpocketguide.com/book
- http://sarabander.github.io/sicp/html/2_002e2.xhtml#g_t2_002e2_002e4
