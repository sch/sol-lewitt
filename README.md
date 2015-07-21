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


## Some notes about closure SVG

Some SVG info: the closure libraries used to have a generic drawing API that's implemented in canvas, SVG, and VML modules. They deprecated it.

The SVG lib is pretty useful still though, and this project uses it for low-level drawing commands without incurring heavy libs like Snap.svg or building it up with a virtual dom library, since we're simply re-drawing everything when changes occur. Maybe unwise. A likely change will be re-implementing the drawing functions with Quiescent.


## Some useful resources

- http://www.w3.org/Graphics/SVG/IG/resources/svgprimer.html
- http://svgpocketguide.com/book/
- https://closure-library.googlecode.com/git-history/docs/class_goog_graphics_SvgGraphics.html
