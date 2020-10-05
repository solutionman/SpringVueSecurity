<template>
  <v-card>
  <v-data-table
      :headers="personsHeaders"
      :items="persons"
      :options.sync="options"
      :sort-by.sync="sortBy"
      :server-items-length="totalPersons"
      :loading="loading"
      :single-expand="singleExpand"
      :expanded.sync="expanded"
      item-key="name"
      show-expand
      class="elevation-1"
      :items-per-page="10"
      :footer-props="footerProps"
  >
    <template v-slot:top>
      <v-toolbar flat>
        <v-toolbar-title>Пользователи системы</v-toolbar-title>
        <v-spacer></v-spacer>
        <v-btn @click="addNewUser" >
          Добавить пользователя
        </v-btn>
        <v-spacer></v-spacer>
        <v-switch
            v-model="singleExpand"
            label="Single expand"
            class="mt-2"
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
  </v-card>
</template>
<script>
import router from "@/router";

export default {
  data () {
    return {
      expanded: [],
      singleExpand: false,
      personsHeaders: [
        {
          text: 'Пользователь',
          align: 'start',
          sortable: false,
          value: 'name',
        },
        { text: 'Фамилия', value: 'familyname' },
        { text: 'Имя', value: 'firstname' },
        { text: 'Отчество', value: 'middlename' },
        { text: 'Дата рождения', value: 'birthday' },
        { text: 'Дополнительные данные', value: 'data-table-expand' },
      ],
      totalPersons: 0,
      loading: true,
      sortBy: 'id',
      persons: [],
      options: {},
      search: '',
      footerProps: {'items-per-page-options': [5, 10, 15, 30, 50, 100]},
    }
  },
  watch: {
    options: {
      handler () {
        this.loading = true;
        let options = {};
        options["sortBy"] = this.options.sortBy;
        options["sortDesc"] = this.options.sortDesc;
        options["page"] = this.options.page;
        options["itemsPerPage"] = this.options.itemsPerPage;
        options["search"] = this.search;
        this.$axios({
          method: 'post',
          url: 'http://localhost:8080/backvuebato/persons',
          data: options
        }).then(response =>{
          console.log(response);
          this.persons = response.data.persons;
          this.totalPersons = response.data.totalPersons;
          this.loading = false;
        }).catch((error)=>{
          console.log(error);
        })
      },
      deep: true
    },
    search: {
      handler () {
        this.loading = true;
        let options = {};
        options["sortBy"] = this.options.sortBy;
        options["sortDesc"] = this.options.sortDesc;
        options["page"] = this.options.page;
        options["itemsPerPage"] = this.options.itemsPerPage;
        options["search"] = this.search;
        this.$axios({
          method: 'post',
          url: 'http://localhost:8080/backvuebato/persons',
          data: options
        }).then(response =>{
          console.log(response);
          this.persons = response.data.persons;
          this.totalPersons = response.data.totalPersons;
          this.loading = false;
        }).catch((error)=>{
          console.log(error);
        })
      },
      deep: true
    }
  },
  methods: {
    addNewUser (){
      console.log("add user");
      router.push({ name: 'NewUser' });
    }
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