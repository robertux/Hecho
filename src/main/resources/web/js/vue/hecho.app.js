var DATE_FORMAT = 'YYYY-MM-DD hh:mm:ss a';
var SORT_BY_DATE = 1;
var SORT_BY_PRIORITY = 2;
moment.locale('es');

var vueApp = new Vue({
    el: '#hecho-app',
    data: {
        categories: [{id: 0, name: "General"}],
        tasks: [],
        currentCategory: 0,
        newTaskName: '',
        newCategoryName: '',
        currentTask: {},
        filterText: '',
        sortMethod: SORT_BY_DATE,
        editCategoriesMode: false
    },
    computed: {
        computedTasks: function() {
            return this.filterTasks(this.tasks, this.filterText);
        }
    },
    methods: {
        loadCategories: function() {
            var self = this;
            $.get("/categories", {}, function(data) {
                if (data.code === 0) {
                    self.categories = data.content.categories;
                }

                if (self.categories.length > 0) {
                    self.loadTasks(self.categories[0]);
                }

                for (var i=0; i<self.categories.length; i++) {
                    self.categories["beingEdited"] = false;
                }
            }, "json");
        },
        nextCategory: function() {
            if (this.currentCategory == (this.categories.length - 1)) {
                this.currentCategory = 0;
            } else {
                this.currentCategory++;
            }
        },
        prevCategory: function() {
            if (this.currentCategory == 0) {
                this.currentCategory = this.categories.length - 1;
            } else {
                this.currentCategory--;
            }
        },
        manageCategories: function() {
            this.editCategoriesMode = !this.editCategoriesMode;
        },
        addCategory: function() {
            var self = this;
            $.post("/categories/" + this.newCategoryName, {}, function(data) {
                if (data.code === 0) {
                    self.newCategoryName = '';
                    self.loadCategories();
                }
            }, "json");
        },
        editCategory: function(cat) {
            cat.beingEdited = true;
        },
        saveCategory: function(cat) {
            var self = this;
            $.put("/categories/" + cat.id + "/" + cat.name, {}, function(data) {
                if (data.code === 0) {
                    self.loadCategories();
                }
            }, "json");
        },
        discardCategory: function(cat) {
            this.loadCategories();
        },
        deleteCategory: function(cat) {
            var self = this;
            $.ajax({url: "/categories/" + cat.id, method: "DELETE", dataType: "json"}).success(function(data) {
                if (data.code === 0) {
                    self.loadCategories();
                }
            });
        },
        loadTasks: function(categoryIndex) {
            var self = this;
            $.get("/categories/" + self.categories[self.currentCategory].id + "/tasks", {}, function(data) {
                if (data.code === 0) {
                    self.tasks = data.content.tasks;
                }
            });
        },
        getRelativeDate(task) {
            if (task.due.trim()) {
                return moment(task.due, DATE_FORMAT).calendar();
            } else {
                return 'En algún momento';
            }
        },
        sortBy: function(sortMethod) {
            this.sortMethod = sortMethod;
        },
        filterTasks: function(taskList, text) {
            if (this.filterText.trim()) {
                return taskList.filter(task => task.name.toUpperCase().indexOf(text.trim().toUpperCase()) !== -1);
            } else {
                return taskList;
            }
        },
        deleteCompletedTasks: function() {
            this.tasks = this.tasks.filter(task => !task.done);
        },
        changePriority: function(task, priority) {
            task.priority = priority;
        },
        addTask: function() {
            if (this.newTaskName.trim()) {
                this.tasks.push({name: this.newTaskName, due: '', priority: 0, done: false});
                this.newTaskName = '';
            }
        },
        markAsDone: function(task) {
            task.done = true;
        },
        editTask: function() {

        },
        saveTask: function() {

        }
    }
});

vueApp.loadCategories();