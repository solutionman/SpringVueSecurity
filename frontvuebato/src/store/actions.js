import axios from 'axios';
import router from '../router';
import EventBus from '../event-bus';

const actions = {
    userSignIn({commit}, payload) {
        const data = {
            username: payload.username,
            password: payload.password,
        };
        commit('setLoading', true);
        // set current ip here
        // axios.post('http://192.168.0.102:8080/backvuebato/login', data)
        axios.post('http://localhost:8080/backvuebato/login', data)
        .then(() => {
            commit('setAuth', true);
            commit('setLoading', false);
            commit('setError', null);
            EventBus.$emit('authenticated', 'User authenticated');
            router.push('/frontvuebato/home');
        })
        .catch((error) => {
            commit('setError', error.message);
            commit('setLoading', false);
        });
    },
    userSignOut({commit}) {
        commit('clearAuth');
        EventBus.$emit('authenticated', 'User not authenticated');
        router.push('/frontvuebato/signIn');
    },
};

export default actions;
