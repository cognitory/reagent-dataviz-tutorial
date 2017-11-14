# DataViz with ClojureScript and Reagent 

Toronto JS Workshop 2017-11-14 


## Demo


## Goal

  - challenge your model of "this is how React is done" by exposing you to alternative ideas

  - some exposure to the clojurescript language


## Clojurescript : Javascript

  - clojurescript = clojure -> javascript 

  - similar:
    - functional
    - similar data types `{ }` `[ ]` 

  - major relevant differences:
    - `( )`
      - brackets come before the function name
      - `(` and `[` used for structure, not `{`
    - new type: `:keywords`
      - like Ruby's symbols
      - mostly used in maps `{ }`
    - implicit return
    - atoms
      - for state

In Clojure:

```clojure
  (def data (atom {:counter 0}))

  (defn increment-counter! [n]
    (swap! data update :counter + n))

  (increment-counter! 8)

  (println (@data :counter))
```

In Javascript:

```javascript
  let data = { counter: 0 };

  function incrementCounter(n) => {
    counter += n;
  }

  incrementCounter(8);

  console.log(data.counter);
```


## Reagent : React

  - cljs ui library (built on top of react)

  - "hiccup" syntax (vs jsx)
    - vectors, keywords, maps
    - very easy to compose, manipulate

  - components are functions (vs usually classes in js)

  - `shouldComponentUpdate` taken care for us
  - rarely make use of component lifecycle functions
  - don't use React's state 


In Clojure + Reagent:

```clojure
(def widgets 
  (r/atom
    {1 {:id 1 :name "foo" :active? true}
     2 {:id 2 :name "bar" :active? false}}))

(defn set-active! [id]
  (swap! widgets assoc-in [id :active?] true))

(defn widget-view [widget]
  [:div.widget 
    {:class (when (widget :active?) "active")
     :on-click (fn [_]
                 (set-active! (widget :id)))}
    (widget :name)])

(defn list-view []
  [:div.list
    (for [widget widgets]
      ^{:key (item :id)}
      [widget-view widget])])
```

In Javascript + React:

```jsx
const widgets = {1: {id: 1, name: "foo", active: true},
                 2: {id: 2, name: "bar", active: false}};

class Widget < Component {
  setActive () {
    widgets[this.props.id]["active"] = true;
  }
  render () {
    return (
      <div class="widget { if(this.props.active?) { "active" } }"
           onClick={ this.setActive }>
        { this.props.name }
      </div>
    );
  }
}

class List < Component {
  render () {
    return (
      <div class="list">
        widgets.map((widget) => {
           <Widget key={widget.id} widget={widget} />
        });
      </div>
    );
  }
}
```


## Architecture

  - typically see a spectrum of: 

```


      *----------------------------------------------------------------------*
 
 all local state                                                    single global state
   - each component keeps its own state                               - named fns to change state 
   - pass data and event handlers via props                           - named fns to extract a subset of state

```

  - today, we will be doing "single global state"


## Data Flow

 - single direction of flow

```
              __\   state   __
             /  /             \
            /                  \
           |                  \|/

     transactions              reactions

(fns that change state)    (fns that read a subset of state)

           /|\                  |
            \                  /
             \__           /__/
                   views    \

             (pure components)
```


## About the Code Editor

  https://www.cognitory.com/

  - three columns: code / workspace / console
  - compiles cljs in your browser
  - Ctrl-Enter to run
  - saves code to localStorage
  - parinfer
    - infers closing parens from indentation 
    - ie. less fiddling with parens
  - limitations
    - can only use namespaces that have been provided for you (ex. reagent, clojure.string)
    - no `:refer`, only `:as`
    - no macros
