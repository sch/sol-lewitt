<p align="center">
  <img title="Wall Drawing 419" alt="Wall Drawing 419" src="https://raw.githubusercontent.com/sch/sol-lewitt/master/resources/public/img/image.jpeg" />
</p>

> The wall is bordered and divided horizontally and vertically into four equal
> parts with a 6-inch (15 cm) black ink band. Each quarter has alternating
> parallel 6-inch (15 cm) bands of white and color ink bands. Upper left: gray;
> upper right: yellow; lower left: red; lower right: blue.
>
> — [Wall Drawing 419](http://www.massmoca.org/lewitt/walldrawing.php?id=419)

Sol LeWitt created conceptual artworks, leaving the interpretation and
implementation to draftsmen. From his "Paragraphs on Conceptual Art":

> "In Conceptual Art the idea or the concept is the most important aspect of
> the work. All planning and decisions are made beforehand and execution is
> a perfunctory affair. The idea is the machine that makes the art."

This project aims to create a declarative grammar for these types of artworks
where the computer can do the drafting. Here's a screenshot:

<p align="center">
  <img title="Wall Drawing 86" alt="Wall Drawing 86" src="https://raw.githubusercontent.com/sch/sol-lewitt/master/resources/public/img/drawings-86.png" />
</p>

## Getting it running

This project requires Java 8 and the latest version of
[leiningen](https://leiningen.org/) installed on your machine. Once installed,
run:

    script/server
    open http://localhost:3449


## Some notes on architecture

One goal of this project is to produce _execution-independent drawing
instructions_. What this means as far as code goes: the drawing algorithms
should produce a sequence of shape maps.

It's the role of a _renderer_ to take this list of shapes, and output an image
depending on what the drawing or environment specifies.

The drawings that are low in complexity use the SVG renderer. The renderer
iterates through the shape map and generates an SVG DOM as a Sablono data
structure. This data structure is then passed to the SVG react component, which
sets the size and viewbox appropriate for the current viewport. The SVG
renderer should be used in cases where easy export, resolution-independence,
and CSS-based additional styling might be useful.

The canvas-based renderer is used for more complex drawings. 10,000 lines in an
SVG document makes the browser slow to a crawl, so for performance reasons, we
want to bake the image by drawing the shapes against a canvas element. The
canvas renderer has some non-standard canvas drawing utilities, such as line
drawing based on manually setting pixels using Bresenham's algorithm, to get
nice, crisp lines. The canvas renderer makes sense when the drawings need to
make use of these fine-tuned rendering features (aliasing, fills, transparency)
or when the drawings a complex enough to require some background processing.

The other renderer is the plotter machine instruction renderer (lives in the
[drawing-machine](https://github.com/sch/drawing-machine) repo)

There are some considerations here around _when_ a drawing renders that haven't
really been addressed. One nice thing about divorcing the renderer from the
shape generation is that it's possible to animate the drawing process. In 86,
it would be nice to draw over the course of several seconds, especially as the
size of the window changes.

Another consideration is the canvas dimensions when generating the list of
shapes. Currently, we feed the pixel size of the canvs into the function. We
also call the shape generation function each time we re-draw. The canvas
renderer re-draws every time the canvas size changes, whereas the SVG renderer
only renders once on mount. Many of the drawing instructions themselves are
unitless. For something like 86, how should we define the length of a line?
Should a "10 inch line" just be 10 pixels long? Should the dimesions fed into
the shape generation be normalized to imperial units?

This gets into a large thing about how we define shapes in the first place.
Many of the drawings use relative sizes, in that they are sized according to
the canvas itself. In that case, the only thing that matters is the aspect
ratio of the canvas.
