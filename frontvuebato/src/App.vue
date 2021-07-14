<template>
  <v-app>
    <v-app-bar
        app
        color="blue-grey"
        dark
    >
      <v-btn
          color="blue-grey darken-4"
          dark
          @click.stop="drawer = !drawer"
      >
        Навигация
      </v-btn>
      <v-spacer />
      <v-toolbar-items >
        <v-btn
            v-for="item in menuItems"
            :key="item.title"
            text
            :to="item.path"
        >
          <v-icon left>
            {{ item.icon }}
          </v-icon>
          {{ item.title }}
        </v-btn>
        <v-btn
            v-if="isAuthenticated"
            text
            @click="userSignOut"
        >
          <v-icon left>
            exit_to_app
          </v-icon>
          Sign Out
        </v-btn>
      </v-toolbar-items>
    </v-app-bar>

    <v-navigation-drawer v-if="isAuthenticated"
                         v-model="drawer"
                         :mini-variant.sync="mini"
                         app
    >
      <v-list-item class="px-2">
        <v-btn
            icon
            @click.stop="mini = !mini"
        >
          <v-icon>mdi-chevron-left</v-icon>
        </v-btn>
      </v-list-item>

      <v-divider></v-divider>

      <v-list dense>
        <v-list-item
            v-for="item in items"
            :key="item.title"
            :to="item.path"
            link
        >
          <v-list-item-icon>
            <v-icon>{{ item.icon }}</v-icon>
          </v-list-item-icon>

          <v-list-item-content>
            <v-list-item-title>{{ item.title }}</v-list-item-title>
          </v-list-item-content>
        </v-list-item>
      </v-list>
    </v-navigation-drawer>

    <v-content >
      <keep-alive>
        <transition name="fade">
          <router-view />
        </transition>
      </keep-alive>
    </v-content>

  </v-app>
</template>

<script>
import EventBus from './event-bus';

export default {
  name: 'App',
  data() {
    return {
      isAuthenticated: false,
      drawer: false,
      mini: false,
    };
  },
  computed: {
      // https://pictogrammers.github.io/@mdi/font/1.1.34/
    items() {
        return [
          { title: 'В начало', path: '/frontvuebato/home', icon: 'mdi-home-city' },
          { title: 'Мой профиль', path: '/frontvuebato/profile', icon: 'mdi-account' },
          { title: 'Пользователи системы', path: '/frontvuebato/persons', icon: 'mdi-account-group-outline' },
          { title: 'Файлы', path: '/frontvuebato/files', icon: 'mdi-file' },
          { title: 'Yandex Maps', path: '/frontvuebato/maps', icon: 'mdi-google-maps' },
        ]
    },
    menuItems() {
      if (this.isAuthenticated) {
        return [
          { title: 'Home', path: '/frontvuebato/home', icon: 'home' },
          { title: 'Profile', path: '/frontvuebato/profile', icon: 'mdi-account' },
        ];
      }
      return [
        { title: 'Home', path: '/frontvuebato/home', icon: 'home' },
        { title: 'Sign In', path: '/frontvuebato/signIn', icon: 'lock_open' },
      ];
    },
  },
  created() {
    this.isAuthenticated = localStorage.getItem('auth');
    // Use localstorage because isAuthenticated from $store is undefined when event is called
    EventBus.$on('authenticated', () => {
      this.isAuthenticated = localStorage.getItem('auth');
    });
  },
  beforeDestroy() {
    EventBus.$off('authenticated');
  },
  methods: {
    userSignOut() {
      this.$store.dispatch('userSignOut');
    },
  },
};
</script>
