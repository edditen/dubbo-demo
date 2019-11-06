package com.tenchael.dubbo.plugin;

public interface Constants {


    String METRICS_KEY = "jmxMetrics";


    class Category {
        public static final String REQUESTS_COUNTER_KEY = "requests";
        public static final String COMPLEMENTS_COUNTER_KEY = "complemented";
        public static final String FAILED_COUNTER_KEY = "failed";
        public static final String HISTOGRAM_KEY = "histogram";
    }
}
