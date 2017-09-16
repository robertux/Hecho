var DATE_FORMAT = 'YYYY-MM-DD hh:mm:ss a';
var SORT_BY_DATE = 1;
var SORT_BY_PRIORITY = 2;
moment.locale('es');

var vueApp = new Vue({
    el: '#hecho-app',
    data: {
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
        loadCategories: function() {
            this.categories = ['General', 'Work', 'Shopping'];

            if (this.categories.length > 0) {
                this.loadTasks(this.categories[0]);
            }
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
        loadTasks: function(categoryIndex) {
            this.tasks.push({name: 'Realizar upgrade de la versión de Sublime Text', due: '2017-09-16 11:00:00 AM', priority: -1, done: false});
            this.tasks.push({name: 'Sacar la basura', due: '2017-09-18 12:30:00 PM', priority: 0, done: false});
            this.tasks.push({name: 'Lavar el carro', due: '2017-09-16 06:45:00 PM', priority: 0, done: false});
            this.tasks.push({name: 'Programar reunión con los proveedores', due: '2017-09-18 09:00:00 AM', priority: 1, done: true});
        },
        getRelativeDate(task) {
            return moment(task.due, DATE_FORMAT).calendar();
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
        sortTasks: function(taskList, sortMethod) {
            switch(sortMethod) {
                case SORT_BY_DATE:
                    return taskList.sort(function(a, b) {
                        var dateA = Date.parse(a.due);
                        var dateB = Date.parse(b.due);
                        return (dateA > dateB? 1: (dateB > dateA? -1: 0));
                    });
                break;
                case SORT_BY_PRIORITY:
                    return taskList.sort(function(a, b) {
                        return (a.priority > b.priority? 1: (b.priority > a.priority? -1: 0));
                    });
                break;
                default:
                    return taskList;
                break;
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
                this.tasks.push({name: this.newTaskName, due: 'En algún momento', priority: 0, done: false});
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