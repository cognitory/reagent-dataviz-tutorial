# Reagent 


## Demo


## Goal

  - challenge your model of "this is how React is done" by exposing you to alternative ideas

  - some exposure to the clojurescript language


## Clojurescript : Javascript

  - clojurescript = clojure -> javascript 

  - similar:
    - functional
    - { }  [  ] 

  - major relevant differences:
    - ( ) 
      - brackets come before the function name
      - mostly just ( and [ 
    - new type: `:keywords`
      - like Ruby's symbols
      - mostly used in maps { }
    - implicit return
    - atoms
      - for state

```clojure
  (def data (atom {:counter 0}))

  (defn increment-counter! [n]
    (swap! data update :counter + n))

  (increment-counter! 8)

  (println (@data :counter))
```


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

  - "shouldComponentUpdate" taken care for us
  - rarely make use of component lifecycle functions
  - don't use React's state 


```clojure
(def widgets 
  (r/atom
    {1 {:id 1 :name "foo" :active? true}
     2 {:id 2 :name "bar" :active? false}}))

(defn widget-view [widget]
  [:div.widget 
    {:class (when (widget :active?) "active")
     :on-click (fn [_]
                 (swap! widgets assoc-in [(widget :id) :active?] true)}
    (widget :name)])

(defn list-view []
  [:div.list
    (for [widget widgets]
      ^{:key (item :id)}
      [widget-view widget])])
```


```jsx
const widgets = {1: {id: 1, name: "foo", active: true},
                 2: {id: 2, name: "bar", active: false}};

class Widget < Component {
  render () {
    return (
      <div class="widget { if(this.props.active?) { "active" } }">
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
    - all local state 
     - each component keeps its own state
     - pass data and event handlers via props

    - single global state
      - the way we'll be doing today


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
    - no :refer, only :as
    - no macros
