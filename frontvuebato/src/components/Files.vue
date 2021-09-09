<template>
    <v-card>
        <form>
            <v-file-input
                v-model="file"
                show-size
                label="File input"
            ></v-file-input>
            <v-btn class="mr-4" @click="submit">Save</v-btn>
        </form>

        <v-data-table
            :headers="headers"
            :items="files"
            :options.sync="options"
            :sort-by.sync="sortBy"
            :server-items-length="totalFiles"
            :loading="loading"
            sort-by="whoInserted"
            class="elevation-1"
        >
            <template v-slot:top>
                <v-toolbar
                    flat
                >
                    <v-toolbar-title>Files</v-toolbar-title>
                    <v-divider
                        class="mx-4"
                        inset
                        vertical
                    ></v-divider>
                    <v-spacer></v-spacer>
                    <v-dialog
                        v-model="dialog"
                        max-width="500px"
                    >
                        <template v-slot:activator="{ on, attrs }">
                            <v-btn
                                color="primary"
                                dark
                                class="mb-2"
                                v-bind="attrs"
                                v-on="on"
                            >
                                New Item
                            </v-btn>
                        </template>
                        <v-card>
                            <v-card-title>
                                <span class="text-h5">{{ formTitle }}</span>
                            </v-card-title>

                            <v-card-text>
                                <v-container>
                                    <v-row>
                                        <v-col
                                            cols="12"
                                            sm="6"
                                            md="4"
                                        >
                                            <v-text-field
                                                v-model="editedItem.name"
                                                label="File name"
                                            ></v-text-field>
                                        </v-col>
                                        <v-col
                                            cols="12"
                                            sm="6"
                                            md="4"
                                        >
                                            <v-text-field
                                                v-model="editedItem.whoInserted"
                                                label="Who added"
                                            ></v-text-field>
                                        </v-col>
                                        <v-col
                                            cols="12"
                                            sm="6"
                                            md="4"
                                        >
                                            <v-text-field
                                                v-model="editedItem.date"
                                                label="Date"
                                            ></v-text-field>
                                        </v-col>
                                    </v-row>
                                </v-container>
                            </v-card-text>

                            <v-card-actions>
                                <v-spacer></v-spacer>
                                <v-btn
                                    color="blue darken-1"
                                    text
                                    @click="close"
                                >
                                    Cancel
                                </v-btn>
                                <v-btn
                                    color="blue darken-1"
                                    text
                                    @click="save"
                                >
                                    Save
                                </v-btn>
                            </v-card-actions>
                        </v-card>
                    </v-dialog>
                    <v-dialog v-model="dialogDelete" max-width="500px">
                        <v-card>
                            <v-card-title class="text-h5">Are you sure you want to delete this file?</v-card-title>
                            <v-card-actions>
                                <v-spacer></v-spacer>
                                <v-btn color="blue darken-1" text @click="closeDelete">Cancel</v-btn>
                                <v-btn color="blue darken-1" text @click="deleteItemConfirm">OK</v-btn>
                                <v-spacer></v-spacer>
                            </v-card-actions>
                        </v-card>
                    </v-dialog>
                </v-toolbar>
            </template>
            <template v-slot:item.actions="{ item }">
                <v-icon
                    small
                    class="mr-2"
                    @click="editItem(item)"
                >
                    mdi-download
                </v-icon>
                <v-icon
                    small
                    @click="deleteItem(item)"
                >
                    mdi-delete
                </v-icon>
            </template>
            <template v-slot:no-data>
                <v-btn
                    color="primary"
                    @click="initialize"
                >
                    Reset
                </v-btn>
            </template>
        </v-data-table>
    </v-card>
</template>
<script>
export default {
    data: () => ({
        file: null,
        dialog: false,
        dialogDelete: false,
        headers: [
            {
                text: 'File name',
                align: 'start',
                value: 'name',
            },
            {text: 'Who added', value: 'whoInserted'},
            {text: 'Date', value: 'insertDate'},
            {text: 'Actions', value: 'actions', sortable: false},
        ],
        files: [],
        totalFiles: 0,
        loading: true,
        options: {},
        sortBy: 'id',
        fileToDelete: [],
        editedIndex: -1,
        editedItem: {
            name: '',
            whoInserted: 0,
            insertDate: 0,
        },
        defaultItem: {
            name: '',
            whoInserted: 0,
            insertDate: 0,
        },
    }),
    // data() {
    //     return {
    //         file: null,
    //     }
    // },
    computed: {
        formTitle() {
            return this.editedIndex === -1 ? 'New Item' : 'Edit Item'
        },
    },

    watch: {
        dialog(val) {
            val || this.close()
        },
        dialogDelete(val) {
            val || this.closeDelete()
        },
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
            url: this.$api_url + 'getFiles',
            data: options
          }).then(response => {
            console.log(response);
            this.files = response.data.files;
            this.totalFiles = response.data.totalFiles;
            this.loading = false;
          }).catch((error) => {
            console.log(error);
          })
        },
        deep: true
      },
    },
    methods: {
        submit() {
            console.log(this.file);
            let formData = new FormData();
            formData.append('file', this.file);
            this.$axios({
                method: 'post',
                url: this.$api_url + 'storeFile',
                // headers: {},
                data: formData
            }).then(response => {
                console.log(response);
            }).catch((error) => {
                console.log(error);
            })
        },
        editItem(item) {
            this.editedIndex = this.files.indexOf(item)
            this.editedItem = Object.assign({}, item)
            this.dialog = true
        },

        deleteItem(item) {
            this.fileToDelete = item;
            this.editedIndex = this.files.indexOf(item)
            this.editedItem = Object.assign({}, item)
            this.dialogDelete = true
        },

        deleteItemConfirm() {
            console.log(this.fileToDelete);
            let data = {};
            data["file"] = this.fileToDelete;
            this.$axios({
                method: 'post',
                url: this.$api_url + 'deleteFile',
                // headers: {},
                data: data
            }).then(response => {
                console.log(response);
            }).catch((error) => {
                console.log(error);
            })
            this.files.splice(this.editedIndex, 1);
            this.closeDelete()
        },

        close() {
            this.dialog = false
            this.$nextTick(() => {
                this.editedItem = Object.assign({}, this.defaultItem)
                this.editedIndex = -1
            })
        },

        closeDelete() {
            this.dialogDelete = false
            this.$nextTick(() => {
                this.editedItem = Object.assign({}, this.defaultItem)
                this.editedIndex = -1
            })
        },

        save() {
            if (this.editedIndex > -1) {
                Object.assign(this.files[this.editedIndex], this.editedItem)
            } else {
                this.files.push(this.editedItem)
            }
            this.close()
        },
    }
}
</script>