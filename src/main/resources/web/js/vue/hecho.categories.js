var DATE_FORMAT = 'YYYY-MM-DD hh:mm:ss a';
var SORT_BY_DATE = 1;
var SORT_BY_PRIORITY = 2;

var vueApp = new Vue({
    el: '#hecho-app',
    data: {
        loading: false,
        categories: [],
        currentCategory: 0,
        filterText: '',
        newCategoryName: ''
    },
    computed: {
        computedCategories: function() {
            return this.filterCategories(this.categories, this.filterText);
        }
    },
    methods: {
        loadCategories: function() {
            var self = this;
            self.loading = true;
            $.get("/api/categories", {}, function(data) {
                if (data.code === 0) {
                    self.categories = data.content.categories;
                } else {
                    this.$message({ message: data.reason, type: 'error'});
                }
                self.loading = false;
            }, "json");
        },
        filterCategories: function(categoryList, text) {
            if (this.filterText.trim()) {
                return categoryList.filter(cat => cat.name.toUpperCase().indexOf(text.trim().toUpperCase()) !== -1);
            } else {
                return categoryList;
            }
        },
        focusSearch: function(key, keyPath) {
            if (key == 1) {
                setTimeout("$('li.el-menu-item div.el-input input').focus()", 300);
            }
        },
        doneCategories: function() {
            window.location.href = "/";
        },
        addCategory: function() {
            var self = this;
            $.post("/api/categories/" + this.newCategoryName, {}, function(data) {
                if (data.code === 0) {
                    self.newCategoryName = '';
                    self.loadCategories();
                } else {
                    this.$message({ message: data.reason, type: 'error'});
                }
            }, "json");
        },
        editCategory: function(cat, index) {
            if (cat.beingEdited) {
                this.discardCategory(cat);
                return;
            }
            cat.originalName = cat.name;
            this.categories.forEach(function(c) { c.beingEdited = false; });
            cat.beingEdited = true;

            setTimeout("$('tr.el-table__row span.catEdit input.el-input__inner')[" + index + "].focus();", 300);
        },
        saveCategory: function(cat) {
            var self = this;
            $.ajax({url: "/api/categories/" + cat.id + "/" + cat.name, method: "PUT", dataType: "json"}).done(function(data) {
                if (data.code === 0) {
                    cat.beingEdited = false;
                    self.loadCategories();
                } else {
                    this.$message({ message: data.reason, type: 'error'});
                }
            });
        },
        discardCategory: function(cat) {
            cat.name = cat.originalName;
            cat.beingEdited = false;
        },
        deleteCategory: function(cat) {
            var self = this;
            $.ajax({url: "/api/categories/" + cat.id, method: "DELETE", dataType: "json"}).done(function(data) {
                if (data.code === 0) {
                    self.loadCategories();
                } else {
                    this.$message({ message: data.reason, type: 'error'});
                }
            });
        }
    }
});

vueApp.loadCategories();