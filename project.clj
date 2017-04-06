(defproject sol-lewitt "0.1.0-SNAPSHOT"
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

                       :figwheel { :on-jsload "sol-lewitt.core/on-js-reload"}

                       :compiler {:main sol-lewitt.core
                                  :asset-path "js/compiled/out"
                                  :output-to "resources/public/js/compiled/sol_lewitt.js"
                                  :output-dir "resources/public/js/compiled/out"
                                  :source-map-timestamp true}}
                      {:id "min"
                       :source-paths ["src"]
                       :compiler {:output-to "resources/public/js/compiled/sol_lewitt.js"
                                  :main sol-lewitt.core
                                  :optimizations :advanced
                                  :pretty-print false}}]}

 :figwheel {;; :http-server-root "public" ;; default and assumes "resources"
            ;; :server-port 3449 ;; default
            ;; :server-ip "127.0.0.1"

            :css-dirs ["resources/public/css"] ;; watch and update CSS

            ;; Start an nREPL server into the running figwheel process
            :nrepl-port 7888})

            ;; Server Ring Handler (optional)
            ;; if you want to embed a ring handler into the figwheel http-kit
            ;; server, this is for simple ring servers, if this
            ;; doesn't work for you just run your own server :)
            ;; :ring-handler hello_world.server/handler

            ;; To be able to open files in your editor from the heads up display
            ;; you will need to put a script on your path.
            ;; that script will have to take a file path and a line number
            ;; ie. in  ~/bin/myfile-opener
            ;; #! /bin/sh
            ;; emacsclient -n +$2 $1
            ;;
            ;; :open-file-command "myfile-opener"

            ;; if you want to disable the REPL
            ;; :repl false

            ;; to configure a different figwheel logfile path
            ;; :server-logfile "tmp/logs/figwheel-logfile.log"
