var DATE_FORMAT = 'YYYY-MM-DD hh:mm:ss a';
var SORT_BY_DATE = 1;
var SORT_BY_PRIORITY = 2;

var vueApp = new Vue({
    el: '#hecho-app',
    data: {
        loading: false,
        providers: []
    },
    methods: {
        loadProviders: function() {
            var self = this;
            self.loading = true;
            $.get("/api/providers", {}, function(data) {
                if (data.code === 0) {
                    self.providers = data.content.providers;
                } else {
                    this.$message({ message: data.reason, type: 'error'});
                }
                self.loading = false;
            }, "json");
        },
        cancelSync: function() {
            window.location.href = "/";
        }
    }
});

vueApp.loadProviders();