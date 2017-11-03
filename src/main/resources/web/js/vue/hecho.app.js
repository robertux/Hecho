var DATE_FORMAT = 'YYYY-MM-DD hh:mm:ss a';
var SORT_BY_DATE = 1;
var SORT_BY_PRIORITY = 2;

var vueApp = new Vue({
    el: '#hecho-app',
    data: {
        categories: [],
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
        /** Categories **/
        loadCategories: function() {
            var self = this;
            $.get("/api/categories", {}, function(data) {
                if (data.code === 0) {
                    self.categories = data.content.categories;
                }

                if (self.categories.length > 0) {
                    self.loadTasks(self.categories[0]);
                }
            }, "json");
        },
        nextCategory: function() {
            if (this.currentCategory == (this.categories.length - 1)) {
                this.currentCategory = 0;
            } else {
                this.currentCategory++;
            }
            this.loadTasks();
        },
        prevCategory: function() {
            if (this.currentCategory == 0) {
                this.currentCategory = this.categories.length - 1;
            } else {
                this.currentCategory--;
            }
            this.loadTasks();
        },
        manageCategories: function() {
            this.editCategoriesMode = !this.editCategoriesMode;
        },
        addCategory: function() {
            var self = this;
            $.post("/api/categories/" + this.newCategoryName, {}, function(data) {
                if (data.code === 0) {
                    self.newCategoryName = '';
                    self.loadCategories();
                }
            }, "json");
        },
        editCategory: function(cat) {
            cat.originalName = cat.name;
            cat.beingEdited = true;
        },
        saveCategory: function(cat) {
            var self = this;
            $.ajax({url: "/api/categories/" + cat.id + "/" + cat.name, method: "PUT", dataType: "json"}).done(function(data) {
                if (data.code === 0) {
                    cat.beingEdited = false;
                    self.loadCategories();
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
                }
            });
        },
        editingOtherCategory: function(cat) {
            return (!cat.beingEdited && this.categories.filter(cat => cat.beingEdited).length > 0);
        },

        /** Tasks **/
        loadTasks: function() {
            var self = this;
            $.get("/api/categories/" + self.categories[self.currentCategory].id + "/tasks/" + self.sortMethod, {}, function(data) {
                if (data.code === 0) {
                    self.tasks = data.content.tasks;
                }
            });
        },
        taskStyle: function({row, rowIndex}) {
            if (this.tasks[rowIndex].status == 'D') {
                return 'done-row';
            } else if (this.tasks[rowIndex].priority > 0) {
                return 'high-priority-row';
            } else if (this.tasks[rowIndex].priority < 0) {
                return 'low-priority-row';
            }
        },
        sortBy: function(sortMethod) {
            this.sortMethod = sortMethod;
            this.loadTasks();
        },
        filterTasks: function(taskList, text) {
            if (this.filterText.trim()) {
                return taskList.filter(task => task.description.toUpperCase().indexOf(text.trim().toUpperCase()) !== -1);
            } else {
                return taskList;
            }
        },
        deleteCompletedTasks: function() {
            this.tasks = this.tasks.filter(task => !task.done);
        },
        priorityIcon: function(task) {
            return (task.priority == 1? "el-icon-star-on": "el-icon-star-off");
        },
        changePriority: function(task) {
            var self = this;
            var priority = (task.priority == 1? 0: 1);
            $.ajax({url: "/api/categories/" + self.categories[self.currentCategory].id + "/tasks/" + task.id, method: "PUT", data: {"priority": priority}, dataType: "json"}).done(function(data) {
                if (data.code === 0) {
                    self.loadTasks();
                }
            });
        },
        addTask: function() {
            var self = this;
            if (this.newTaskName.trim()) {
                $.post("/api/categories/" + self.categories[self.currentCategory].id + "/tasks/" + self.newTaskName, {}, function(data) {
                    if (data.code === 0) {
                        self.newTaskName = '';
                        self.loadTasks();
                    }
                })
            }
        },
        markAsDone: function(task) {
            var self = this;
            $.ajax({url: "/api/categories/" + self.categories[self.currentCategory].id + "/tasks/" + task.id, method: "PUT", data: {status: "D"}, dataType: "json"}).done(function(data) {
                if (data.code === 0) {
                    self.loadTasks();
                }
            });
        },
        isDone: function(task) {
            return task.status == 'D';
        },
        editTask: function(task) {
            task.beingEdited = true;
        },
        saveTask: function(task) {
            var self = this;
            $.ajax({url: "/api/categories/" + self.categories[self.currentCategory].id + "/tasks/" + task.id, method: "PUT", data: {description: task.description}, dataType: "json"}).done(function(data) {
                if (data.code === 0) {
                    self.loadTasks();
                }
            });
        },
        discardTask: function(task) {
            task.beingEdited = false;
        },
        editingATask: function() {
            return (this.tasks.filter(t => t.beingEdited).length > 0);
        },
    },
    directives: {
        focus: {
          inserted: function (el) {
            el.focus()
          }
        }
    }
});

vueApp.loadCategories();
