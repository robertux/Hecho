var vueApp = new Vue({
    el: '#hecho-app',
    data: {
        categories: [],
        tasks: [],
        currentCategory: 0,
        currentTask: {}
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
            this.tasks.push({name: 'Realizar upgrade de la versión de Sublime Text', due: 'Mañana, 11:00 p.m.', priority: -1, done: false});
            this.tasks.push({name: 'Sacar la basura', due: '18-sep 12:30 p.m.', priority: 0, done: false});
            this.tasks.push({name: 'Lavar el carro', due: 'Hoy, 6:45 p.m.', priority: 0, done: false});
            this.tasks.push({name: 'Programar reunión con los proveedores', due: 'El próximo lunes, 9:00 p.m.', priority: 1, done: true});
        },
        changePriority: function(task) {
            task.priority = (task.priority == 1? -1: (task.priority == 0? 1: 0));
        },
        addTask: function() {

        },
        editTask: function() {

        },
        saveTask: function() {

        }
    }
});

vueApp.loadCategories();