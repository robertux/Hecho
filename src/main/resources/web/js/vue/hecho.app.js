var DATE_FORMAT = 'YYYY-MM-DD hh:mm:ss a';
var SORT_BY_DATE = 1;
var SORT_BY_PRIORITY = 2;

var vueApp = new Vue({
    el: '#hecho-app',
    data: {
        loading: false,
        categories: [],
        tasks: [],
        currentCategory: 0,
        newTaskName: '',
        currentTask: {},
        filterText: '',
        sortMethod: SORT_BY_DATE
    },
    computed: {
        computedTasks: function() {
            return this.sortTasks(this.filterTasks(this.tasks, this.filterText), this.sortMethod);
        }
    },
    methods: {
        /** Categories **/
        loadCategories: function() {
            var self = this;
            self.loading = true;
            $.get("/api/categories", {}, function(data) {
                if (data.code === 0) {
                    self.categories = data.content.categories;
                } else {
                    this.$message({ message: data.reason, type: 'error'});
                }

                if (self.categories.length > 0) {
                    self.loadTasks(self.categories[0]);
                }
                self.loading = false;
            }, "json");
        },
        selectCategory: function(newIndex, oldIndex) {
            this.currentCategory = newIndex;
            this.loadTasks();
        },
        manageCategories: function() {
            window.location.href = "/categories/";
        },
        /** Tasks **/
        loadTasks: function() {
            var self = this;
            self.loading = true;
            $.get("/api/categories/" + self.categories[self.currentCategory].id + "/tasks/" + self.sortMethod, {}, function(data) {
                if (data.code === 0) {
                    self.tasks = data.content.tasks;
                } else {
                    this.$message({ message: data.reason, type: 'error'});
                }
                self.loading = false;
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
        sortTasks: function(taskList, sortMethod) {
            if (sortMethod == SORT_BY_PRIORITY) {
                return taskList.sort(function(a, b) { return b.priority - a.priority; });
            } else if (sortMethod == SORT_BY_DATE) {
                return taskList.sort(function(a, b) { return b.date - a.date; });
            }
        },
        focusSearch: function(key, keyPath) {
            if (key == 1) {
                setTimeout("$('li.el-menu-item div.el-input input').focus()", 300);
            }
        },
        deleteCompletedTasks: function() {
            var self = this;
            $.ajax({url: "/api/categories/" + self.categories[self.currentCategory].id + "/doneTasks/", method: "DELETE", dataType: "json"}).done(function(data) {
                if (data.code === 0) {
                    self.loadTasks();
                } else {
                    this.$message({ message: data.reason, type: 'error'});
                }
            });
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
                } else {
                    this.$message({ message: data.reason, type: 'error'});
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
                    } else {
                        this.$message({ message: data.reason, type: 'error'});
                    }
                })
            }
        },
        markAsDone: function(task) {
            var self = this;
            $.ajax({url: "/api/categories/" + self.categories[self.currentCategory].id + "/tasks/" + task.id, method: "PUT", data: {status: "D"}, dataType: "json"}).done(function(data) {
                if (data.code === 0) {
                    self.loadTasks();
                } else {
                    this.$message({ message: data.reason, type: 'error'});
                }
            });
        },
        isDone: function(task) {
            return task.status == 'D';
        },
        editTask: function(task, index) {
            if (task.beingEdited) {
                this.discardTask(task);
                return;
            }

            task.originalDescription = task.description;
            this.tasks.forEach(function(t) { t.beingEdited = false; });
            task.beingEdited = true;

            setTimeout("$('tr.el-table__row span.taskEdit input.el-input__inner')[" + index + "].focus();", 300);
        },
        saveTask: function(task) {
            var self = this;
            $.ajax({url: "/api/categories/" + self.categories[self.currentCategory].id + "/tasks/" + task.id, method: "PUT", data: {description: task.description, date: task.date}, dataType: "json"}).done(function(data) {
                if (data.code === 0) {
                    self.loadTasks();
                } else {
                    this.$message({ message: data.reason, type: 'error'});
                }
            });
        },
        discardTask: function(task) {
            task.description = task.originalDescription;
            task.beingEdited = false;
        },
        setTaskDate: function(index) {
            setTimeout("$('div.el-date-editor input.el-input__inner')[" + index + "].focus();", 300);
        },
        chooseProvider: function() {
            window.location.href = "/providers/";
        },
        checkUrlMessages: function() {
            var code = $.QueryString["code"];
            var reason = $.QueryString["reason"];

            if (code && reason) {
                this.$message({ message: reason, type: code == 0?'success': 'error'});
            }
        }
    }
});

vueApp.checkUrlMessages();
vueApp.loadCategories();