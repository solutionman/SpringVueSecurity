<template>
    <v-container>
        <v-layout row>
            <v-flex
                xs12
                class="text-center"
                mt-5
                pa-2
            >
                <v-card-title>
                    Редактирование профиля
                    <v-spacer></v-spacer>
                </v-card-title>

                <ValidationObserver ref="observer" v-slot="{ validate, reset }">
                    <form>
                        <ValidationProvider v-slot="{ errors }" name="Имя" rules="required|min:1|max:30">
                            <v-text-field
                                v-model="first_name"
                                :counter="30"
                                :error-messages="errors"
                                label="Имя"
                                required
                            ></v-text-field>
                        </ValidationProvider>
                        <ValidationProvider v-slot="{ errors }" name="Фамилия" rules="required|min:1|max:30">
                            <v-text-field
                                v-model="second_name"
                                :counter="30"
                                :error-messages="errors"
                                label="Фамилия"
                                required
                            ></v-text-field>
                        </ValidationProvider>
                        <ValidationProvider v-slot="{ errors }" name="Отчество" rules="required|min:1|max:30">
                            <v-text-field
                                v-model="middle_name"
                                :counter="30"
                                :error-messages="errors"
                                label="Отчество"
                                required
                            ></v-text-field>
                        </ValidationProvider>
                        <ValidationProvider v-slot="{ errors }" name="Дата рождения" rules="required">
                            <v-text-field
                                v-model="birthday"
                                :counter="30"
                                :error-messages="errors"
                                label="Дата рождения"
                            ></v-text-field>
                        </ValidationProvider>
                        <ValidationProvider v-slot="{ errors }" name="email" rules="required|email">
                            <v-text-field
                                v-model="email"
                                :error-messages="errors"
                                label="E-mail"
                                required
                            ></v-text-field>
                        </ValidationProvider>

                        <v-btn class="mr-4" @click="submit">Применить</v-btn>
                        <v-btn @click="clear">Отмена</v-btn>
                    </form>
                </ValidationObserver>

            </v-flex>

            <v-flex
                xs8
                offset-xs2
                class="text-left"
                mt-5
            >
            </v-flex>

        </v-layout>
    </v-container>
</template>

<script>
import {required, email, max, min} from 'vee-validate/dist/rules'
import {extend, ValidationObserver, ValidationProvider, setInteractionMode} from 'vee-validate'

setInteractionMode('eager')

extend('required', {
    ...required,
    message: '{_field_} Не может быть пустым',
})

extend('max', {
    ...max,
    message: '{_field_} не может быть более {length} знаков',
})

extend('min', {
    ...min,
    message: '{_field_} не может быть менее {length} знаков',
})

extend('email', {
    ...email,
    message: 'Введите действующий адрес почты',
})
export default {
    components: {
        ValidationProvider,
        ValidationObserver,
    },
    data() {
        return {
            first_name: '',
            second_name: '',
            middle_name: '',
            birthday: '',
            name: '',
            email: '',
            select: null,
        }
    },
    mounted() {
        this.$axios({
            method: 'post',
            url: this.$api_url + 'profile',
        }).then(response => {
            // console.log( response );
            this.first_name = response.data.first_name;
            this.second_name = response.data.second_name;
            this.middle_name = response.data.middle_name;
            this.birthday = response.data.birthday == null ? 'Нет даты' : this.$moment(response.data.birthday).locale('ru').format('DD MMMM YYYY');
            this.email = response.data.email;
        }).catch((error) => {
            console.log(error);
        })
    },
    methods: {
        async submit() {
            let isValid = await this.$refs.observer.validate();
            // console.log(isValid);
            if (isValid) {
                let formValues = {};
                formValues["first_name"] = this.first_name;
                formValues["second_name"] = this.second_name;
                formValues["middle_name"] = this.middle_name;
                formValues["email"] = this.email;
                formValues["birthday"] = this.birthday;
                this.$axios({
                    method: 'post',
                    url: this.$api_url + 'edit_profile',
                    // headers: {},
                    data: formValues
                }).then(response => {
                    // console.log(response);
                    this.first_name = response.data.first_name;
                    this.second_name = response.data.second_name;
                    this.middle_name = response.data.middle_name;
                    this.birthday = response.data.birthday == null ? 'Нет даты' : this.$moment(response.data.birthday).locale('ru').format('DD MMMM YYYY');
                    this.email = response.data.email;
                }).catch((error) => {
                    console.log(error);
                })
            }
        },
        clear() {
            this.$axios({
                method: 'post',
                url: this.$api_url + 'profile'
            }).then(response => {
                // console.log( response );
                this.first_name = response.data.first_name;
                this.second_name = response.data.second_name;
                this.middle_name = response.data.middle_name;
                this.birthday = response.data.birthday == null ? 'Нет даты' : this.$moment(response.data.birthday).locale('ru').format('DD MMMM YYYY');
                this.email = response.data.email;
            }).catch((error) => {
                console.log(error);
            })
            this.$refs.observer.reset()
        },
    },
}

</script>