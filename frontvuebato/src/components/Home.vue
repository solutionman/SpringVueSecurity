<template>
  <div class="text-center">

    <h2 v-if="!isAuthenticated">
      Для работы в системе авторизуйтесь пожалуйста.
    </h2>
    <h2 v-if="isAuthenticated">
      Здравствуйте, вы зашли как {{ data.email }}
    </h2>

    <h2 v-if="!isAuthenticated">
      <router-link
        to="/frontvuebato/signIn"
      >
        На страницу авторизации.
      </router-link>
    </h2>
    <h2 v-if="isAuthenticated">
      <router-link
          to="/frontvuebato/home"
      >
        Домашняя страница
      </router-link>
      <v-spacer></v-spacer>
      <router-link
          to="/frontvuebato/persons"
      >
        Пользователи системы
      </router-link>
    </h2>
  </div>
</template>

<script>
export default {
  name: 'Home',
  data() {
    return {
      isAuthenticated: false,
      data: '',
      status: '',
    };
  },
  created() {
    this.getHomePageInformation();
    this.isAuthenticated = localStorage.getItem('auth');
  },
  methods: {
    getHomePageInformation() {
      this.$axios
        .post('http://localhost:8080/backvuebato/profile')
        .then((response) => {
          // console.log('Get response: ', response.data);
          this.data = response.data;
        })
        .catch((error) => {
          this.alert = true;
          console.error(error);
        });
    },
  },
};
</script>

<style scoped>
  a {
    color: #37bfe6;
  }
</style>
