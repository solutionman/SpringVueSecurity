<template>
    <v-card>
        <v-data-table
            v-model="selected"
            :headers="personsHeaders"
            :items="persons"
            :options.sync="options"
            :sort-by.sync="sortBy"
            :server-items-length="totalPersons"
            :loading="loading"
            :single-expand="singleExpand"
            :expanded.sync="expanded"
            :single-select="singleSelect"
            item-key="name"
            show-expand
            show-select
            class="elevation-1"
            :items-per-page="10"
            :footer-props="footerProps"
        >
            <template v-slot:top>
                <v-toolbar flat>
                    <v-toolbar-title>Пользователи</v-toolbar-title>
                    <v-spacer></v-spacer>
                    <v-btn
                        color="#74C2F2"
                        @click="addNewUser">
                        Добавить
                    </v-btn>
                    <v-spacer></v-spacer>
                    <v-btn
                        color="#A3E4D7"
                        @click="editUser">
                        Редактировать
                    </v-btn>
                    <v-spacer></v-spacer>
                    <v-btn
                        color="#F7C5C5"
                        @click="deleteUser">
                        Удалить
                    </v-btn>
                    <v-spacer></v-spacer>
                    <v-switch
                        v-model="singleExpand"
                        label="Single expand"
                        class="mt-2"
                    ></v-switch>
                    <v-switch
                        v-model="singleSelect"
                        label="Single select"
                        class="pa-3"
                    ></v-switch>
                </v-toolbar>
                <v-text-field
                    v-model="search"
                    append-icon="mdi-magnify"
                    label="Поиск по Ф.И.О."
                    single-line
                    hide-details
                    clearable
                ></v-text-field>
            </template>
            <template v-slot:expanded-item="{ headers, item }">
                <td :colspan="headers.length">
                    Дополнительная информация по {{ item.name }}: {{ item.additionalData }}
                </td>
            </template>
        </v-data-table>

        <v-dialog
            v-model="dialog"
            persistent
            max-width="600px"
        >
            <template v-slot:activator="{ on, attrs }">
                <v-btn
                    color="primary"
                    dark
                    v-bind="attrs"
                    v-on="on"
                >
                    Generate
                </v-btn>
            </template>
            <v-card>
                <v-card-title>
                    <span class="headline">User Profile</span>
                </v-card-title>
                <v-card-text>
                    <v-container>
                        <v-row>
                            <v-col cols="12">
                                <v-text-field
                                    v-model="amount"
                                    :rules="[rules.required, rules.min]"
                                    label="Amount of users*"
                                    hint="At least 1 number"
                                    required
                                ></v-text-field>
                            </v-col>
                        </v-row>
                    </v-container>
                    <small>*indicates required field</small>
                </v-card-text>
                <v-card-actions>
                    <v-spacer></v-spacer>
                    <v-btn
                        color="blue darken-1"
                        text
                        @click="cancelGenerateUsers"
                    >
                        Cancel
                    </v-btn>
                    <v-btn
                        color="blue darken-1"
                        text
                        @click="generateUsers"
                    >
                        Generate
                    </v-btn>
                </v-card-actions>
            </v-card>
        </v-dialog>
    </v-card>
</template>
<script>
import router from "@/router";

export default {
    data() {
        return {
            expanded: [],
            singleExpand: true,
            personsHeaders: [
                {
                    text: 'Пользователь',
                    align: 'start',
                    sortable: false,
                    value: 'name',
                },
                {text: 'Фамилия', value: 'familyname'},
                {text: 'Имя', value: 'firstname'},
                {text: 'Отчество', value: 'middlename'},
                {text: 'Дата рождения', value: 'birthday'},
                {text: 'Дополнительные данные', value: 'data-table-expand'},
            ],
            totalPersons: 0,
            loading: true,
            sortBy: 'id',
            persons: [],
            selected: [],
            singleSelect: true,
            options: {},
            search: '',
            amount: 0,
            dialog: false,
            footerProps: {'items-per-page-options': [5, 10, 15, 30, 50, 100]},
            rules: {
                required: value => !!value || 'Required.',
                min: v => v.length >= 1 || 'Min 1 character',
            },
        }
    },
    watch: {
        options: {
            handler() {
                this.loading = true;
                let options = {};
                options["sortBy"] = this.options.sortBy;
                options["sortDesc"] = this.options.sortDesc;
                options["page"] = this.options.page;
                options["itemsPerPage"] = this.options.itemsPerPage;
                options["search"] = this.search;
                this.$axios({
                    method: 'post',
                    url: this.$api_url + 'persons',
                    data: options
                }).then(response => {
                    console.log(response);
                    this.persons = response.data.persons;
                    this.totalPersons = response.data.totalPersons;
                    this.loading = false;
                }).catch((error) => {
                    console.log(error);
                })
            },
            deep: true
        },
        search: {
            handler() {
                this.loading = true;
                let options = {};
                options["sortBy"] = this.options.sortBy;
                options["sortDesc"] = this.options.sortDesc;
                options["page"] = this.options.page;
                options["itemsPerPage"] = this.options.itemsPerPage;
                options["search"] = this.search;
                this.$axios({
                    method: 'post',
                    url: this.$api_url + 'persons',
                    data: options
                }).then(response => {
                    console.log(response);
                    this.persons = response.data.persons;
                    this.totalPersons = response.data.totalPersons;
                    this.loading = false;
                }).catch((error) => {
                    console.log(error);
                })
            },
            deep: true
        }
    },
    methods: {
        addNewUser() {
            console.log("add user");
            router.push({name: 'NewUser'});
        },
        editUser() {
            console.log("edit user");
            console.log(this.selected[0])
            let person = this.selected[0];
            console.log(person["id"]);
            let id = person["id"];
            if (id != null) {
                router.push({name: 'EditUser', params: {person}});
            }
        },
        deleteUser() {
            console.log("delete user");
            console.log(this.selected);
            this.loading = true;
            let options = {};
            options["sortBy"] = this.options.sortBy;
            options["sortDesc"] = this.options.sortDesc;
            options["page"] = this.options.page;
            options["itemsPerPage"] = this.options.itemsPerPage;
            options["search"] = this.search;
            options["selected"] = this.selected;
            console.log(options);
            this.$axios({
                method: 'post',
                url: this.$api_url + 'delete',
                data: options
            }).then(response => {
                console.log(response);
                this.persons = response.data.persons;
                this.totalPersons = response.data.totalPersons;
                this.loading = false;
            }).catch((error) => {
                console.log(error);
            })
        },
        generateUsers() {
            // console.log("generating users");
            this.dialog = false
            this.loading = true;
            let options = {};
            options["amount"] = this.amount;
            options["sortBy"] = this.options.sortBy;
            options["sortDesc"] = this.options.sortDesc;
            options["page"] = this.options.page;
            options["itemsPerPage"] = this.options.itemsPerPage;
            options["search"] = this.search;
            options["selected"] = this.selected;
            console.log(options);
            this.$axios({
                method: 'post',
                url: this.$api_url + 'generate',
                data: options
            }).then(response => {
                this.persons = response.data.persons;
                this.totalPersons = response.data.totalPersons;
                this.loading = false;
                console.log(response);
            }).catch((error) => {
                this.loading = false;
                console.log(error);
            })
        },
        cancelGenerateUsers() {
            this.dialog = false;
            this.pass = '';
            this.pass_confirm = '';
        },
    }
    // mounted() {
    //   this.loading = true;
    //   let options = {};
    //   options["sortBy"] = this.options.sortBy;
    //   options["sortDesc"] = this.options.sortDesc;
    //   options["page"] = this.options.page;
    //   options["itemsPerPage"] = this.options.itemsPerPage;
    //   options["search"] = this.search;
    //   this.$axios({
    //     method: 'post',
    //     url: 'http://localhost:8080/backvuebato/persons',
    //     data: options
    //   }).then(response =>{
    //     // console.log(response);
    //     this.persons = response.data.persons;
    //     this.totalPersons = response.data.totalPersons;
    //     this.loading = false;
    //   }).catch((error)=>{
    //     console.log(error);
    //   })
    // }
}
</script>