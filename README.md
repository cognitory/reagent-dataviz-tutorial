# Frontend UI and Data Viz with Clojurescript + Reagent 

  - go to the web editor: https://www.cognitory.com/

## Clean Slate

 - create a namespace
 - add a `println` 

## [0-console-hello-world.cljs]

  - add app-view
  - require reagent, define render, call render

## [1-reagent-hello-world.cljs]

  - create `db` ratom w/ `:active-year` 
  - create `active-year` reaction
  - create `slider-view` showing `@active-year`
  - add `slider-view` to `app-view`

  - create `set-active-year!` transaction
  - add `:input` to `slider-view` w/ `:value` and `:on-change`

## [2-active-year-slider.cljs]

  - use `h/get-data` to fetch and pprint data
  - initialize `:data` to `[]`
  - create `set-data!` transaction
  - create `data` reaction 
  - create `table-view` w/ loop over `@data`
  - add `table-view` to `app-view`

## [3-data-table.cljs]

  - create `active-year-data` reaction
  - use `@active-year-data` instead of `@data` in `table-view`

## [4-filter-data.cljs]

  - create `graph-view` depending on `@data` and `@active-year-data`
  - add `graph-view` to `app-view`

## [5-graph-data.cljs]

Suggested extensions:

  - click on table column to sort it
  - hover over table row or graph circle -> highlight corresponding graph circle / table row

