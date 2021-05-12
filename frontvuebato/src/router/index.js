import Vue from 'vue';
import Router from 'vue-router';
import Home from '../components/Home.vue';
import NotFound from '../components/NotFound.vue';
import SignIn from '../components/SignIn.vue';
import Profile from "@/components/Profile";
import Persons from "@/components/Persons";
import NewUser from "@/components/NewUser";
import Maps from "@/components/Maps";
import EditUser from "../components/EditUser";


Vue.use(Router);

const router = new Router({
  mode: 'history',
  routes: [
    {
      path: '/',
      redirect: '/frontvuebato/home',
    },
    {
      path: '/frontvuebato/home',
      name: 'Home',
      component: Home,
      // meta: { nonRequiresAuth: true },
    },
    {
      path: '/frontvuebato/profile',
      name: 'Profile',
      component: Profile,
    },
    {
      path: '/frontvuebato/maps',
      name: 'Maps',
      component: Maps,
    },
    {
      path: '/frontvuebato/newUser',
      name: 'NewUser',
      component: NewUser,
    },
    {
      path: '/frontvuebato/editUser/:id',
      name: 'EditUser',
      component: EditUser,
    },
    {
      path: '/frontvuebato/persons',
      name: 'Persons',
      component: Persons,
    },
    {
      path: '/frontvuebato/signIn',
      name: 'signIn',
      component: SignIn,
      meta: { loginPage: true, nonRequiresAuth: true },
    },
    {
      path: '*',
      component: NotFound,
    },
  ],
});

router.beforeEach((to, from, next) => {
  const requiresAuth = !to.matched.some((record) => record.meta.nonRequiresAuth);
  const isLoginPage = to.matched.some((record) => record.meta.loginPage);
  const isAuthenticated = localStorage.getItem('auth');
  if (requiresAuth && !isAuthenticated) {
    next('/frontvuebato/signIn');
  } else if (isLoginPage && isAuthenticated) {
    router.push('/frontvuebato/home');
  }
  next();
});

export default router;
