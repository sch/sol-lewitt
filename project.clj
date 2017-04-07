(defproject lewitt "0.1.0-SNAPSHOT"
 :description "Geometric drawings generated from descriptions"
 :url "http://github.com/sch/sol-lewitt"
 :license {:name "Eclipse Public License"
           :url "http://www.eclipse.org/legal/epl-v10.html"}

 :dependencies [[org.clojure/clojure "1.8.0"]
                [org.clojure/clojurescript "1.9.494"]
                [org.clojure/core.async "0.3.442"]
                [cljsjs/react "15.4.2-2"]
                [cljsjs/react-dom "15.4.2-2"]
                [sablono "0.8.0"]
                [brutha "0.2.1"]
                [bidi "2.0.16"]
                [kibu/pushy "0.3.7"]
                [figwheel-sidecar "0.5.8"]
                [com.cemerick/piggieback "0.2.1"]]

 :plugins [[lein-cljsbuild "1.1.5"]
           [lein-figwheel "0.5.9"]]

 :source-paths ["src"]

 :repl-options {:nrepl-middleware [cemerick/piggieback/wrap-cljs-repl]}

 :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"]

 :cljsbuild {
             :builds [{:id "dev"
                       :source-paths ["src"]

                       :figwheel { :on-jsload "lewitt.core/on-js-reload"}

                       :compiler {:main lewitt.core
                                  :asset-path "/js/compiled/out"
                                  :output-to "resources/public/js/compiled/lewitt.js"
                                  :output-dir "resources/public/js/compiled/out"
                                  :source-map-timestamp true}}
                      {:id "min"
                       :source-paths ["src"]
                       :compiler {:output-to "resources/public/js/compiled/lewitt.js"
                                  :main lewitt.core
                                  :optimizations :advanced
                                  :pretty-print false}}]}

 :figwheel {:css-dirs ["resources/public/css"]
            :nrepl-port 7888
            :ring-handler lewitt.server/handler})
