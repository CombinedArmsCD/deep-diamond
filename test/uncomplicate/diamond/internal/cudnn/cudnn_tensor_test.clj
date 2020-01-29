(ns uncomplicate.diamond.internal.cudnn.cudnn-tensor-test
  (:require [midje.sweet :refer [facts throws =>]]
            [uncomplicate.commons.core :refer [with-release]]
            [uncomplicate.neanderthal.core :refer [dim]]
            [uncomplicate.diamond.tensor :refer [with-diamond *diamond-factory* tensor]]
            [uncomplicate.diamond.internal.cudnn.factory :refer [cudnn-factory]]
            [uncomplicate.diamond.internal.dnnl.factory :refer [dnnl-factory]]
            [uncomplicate.diamond.tensor-test :refer :all])
  (:import clojure.lang.ExceptionInfo))

(defn test-cudnn-create [fact]
  (facts
   "Test cuDNN specific constraints."
   (tensor fact [0 1 1 1] :float :nchw) => (throws ExceptionInfo)
   (tensor fact [2 3] :int :nc) => (throws ExceptionInfo)
   (tensor fact [2 3] :long :nc) => (throws ExceptionInfo)
   (with-release [t1 (tensor fact [2 3 2 2] :double :nchw)]
     (dim t1) => 24)))

(with-release [dnnl-fact (dnnl-factory)]
  (with-diamond cudnn-factory []

    (test-tensor *diamond-factory*)
    (test-create *diamond-factory*)
    (test-cudnn-create *diamond-factory*)
    (test-equality *diamond-factory*)
    (test-release *diamond-factory*)
    (test-transfer *diamond-factory* dnnl-fact)
    (test-contiguous *diamond-factory*)
    (test-subtensor *diamond-factory*)
    (test-transformer *diamond-factory*)
    (test-pull-different *diamond-factory*)
    (test-pull-same *diamond-factory*)
    (test-push-different *diamond-factory*)
    (test-push-same *diamond-factory*)
    (test-batcher *diamond-factory*)
    (test-shuffler *diamond-factory*)))
