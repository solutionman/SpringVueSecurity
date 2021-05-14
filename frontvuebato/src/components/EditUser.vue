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
                    Edit user.
                    <v-spacer></v-spacer>
                </v-card-title>

                <ValidationObserver ref="observer" v-slot="{ validate, reset }">
                    <form>
                          <ValidationProvider v-slot="{ errors }" name="Username" rules="required|min:3|max:30">
                            <v-text-field
                                v-model="username"
                                :counter="30"
                                :error-messages="errors"
                                label="Username"
                                required
                            ></v-text-field>
                          </ValidationProvider>
                        <ValidationProvider v-slot="{ errors }" name="Password" rules="required|min:8|max:30">
                          <v-text-field
                              v-model="password"
                              :counter="30"
                              :error-messages="errors"
                              label="Password"
                              required
                          ></v-text-field>
                        </ValidationProvider>
                        <ValidationProvider v-slot="{ errors }" name="Confirm Password" rules="required|min:8|max:30">
                          <v-text-field
                              v-model="confirm_password"
                              :counter="30"
                              :error-messages="errors"
                              label="Confirm password"
                              required
                          ></v-text-field>
                        </ValidationProvider>
                        <ValidationProvider v-slot="{ errors }" name="Роль" rules="required|min:1|max:30">
                          <v-select
                              v-model="selectedRoles"
                              :items="roles"
                              :menu-props="{ maxHeight: '400' }"
                              label="Roles"
                              multiple
                              hint="Choose roles"
                              persistent-hint
                          ></v-select>
                        </ValidationProvider>
                        <ValidationProvider v-slot="{ errors }" name="Имя" rules="required|min:1|max:30">
                            <v-text-field
                                    v-model="first_name"
                                    :counter="30"
                                    :error-messages="errors"
                                    label="First name"
                                    required
                            ></v-text-field>
                        </ValidationProvider>
                        <ValidationProvider v-slot="{ errors }" name="Фамилия" rules="required|min:1|max:30">
                            <v-text-field
                                    v-model="second_name"
                                    :counter="30"
                                    :error-messages="errors"
                                    label="Second name"
                                    required
                            ></v-text-field>
                        </ValidationProvider>
                      <ValidationProvider v-slot="{ errors }" name="Отчество" rules="required|min:1|max:30">
                        <v-text-field
                            v-model="middle_name"
                            :counter="30"
                            :error-messages="errors"
                            label="Middle name"
                            required
                        ></v-text-field>
                      </ValidationProvider>
                      <ValidationProvider v-slot="{ errors }" name="Дата рождения" rules="required">
                        <v-text-field
                            v-model="birthday"
                            :counter="30"
                            :error-messages="errors"
                            label="Дата рождения"
                            required
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

                        <v-btn class="mr-4" @click="submit">Save</v-btn>
                        <v-btn @click="clear">Clear</v-btn>
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
import {email, max, min, required} from 'vee-validate/dist/rules'
import {extend, setInteractionMode, ValidationObserver, ValidationProvider} from 'vee-validate'
import router from "@/router";

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
        data () {
            return {
                username: '',
                password: '',
                confirm_password: '',
                first_name: '',
                second_name: '',
                middle_name: '',
                birthday: '',
                name: '',
                email: '',
                select: null,
                roles: [],
                selectedRoles: [],
            }
        },
        mounted () {
          console.log(this.$route.params);
          let person = this.$route.params;
          // TODO reload page removes params
          // for now just return back to Persons after refresh page
          if(Object.keys(person).length === 0 && person.constructor === Object){
            router.push({ name: 'Persons'});
          }
            this.$axios({
                method: 'post',
                url: 'http://localhost:8080/backvuebato/editUser',
                data: person
            }).then(response =>{
                console.log( response );
                this.roles = response.data.roles;
                this.username = response.data.profile.username;
                this.first_name = response.data.profile.first_name;
                this.second_name = response.data.profile.second_name;
                this.middle_name = response.data.profile.middle_name;
                this.birthday = response.data.profile.birthday;
                this.selectedRoles = response.data.userRoles;
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
                    formValues["username"] = this.username;
                    formValues["password"] = this.password;
                    formValues["first_name"] = this.first_name;
                    formValues["second_name"] = this.second_name;
                    formValues["middle_name"] = this.middle_name;
                    formValues["birthday"] = this.birthday;
                    formValues["email"] = this.email;
                    formValues["roles"] = this.selectedRoles;
                    this.$axios({
                        method: 'post',
                        url: 'http://localhost:8080/backvuebato/doEditUser',
                        // headers: {},
                        data: formValues
                    }).then(response => {
                        console.log(response);
                        // router.push({ name: 'Persons' });
                        // this.first_name = response.data.first_name;
                        // this.second_name = response.data.second_name;
                        // this.middle_name = response.data.middle_name;
                        // this.birthday = response.data.birthday;
                        // this.email = response.data.email;
                    }).catch((error) => {
                        console.log(error);
                    })
                }
            },
            clear () {
              // TODO reload page removes params
              console.log(this.$route.params);
              let person = this.$route.params;
              this.$axios({
                method: 'post',
                url: 'http://localhost:8080/backvuebato/editUser',
                data: person
              }).then(response =>{
                console.log( response );
                this.roles = response.data.roles;
                this.username = response.data.profile.username;
                this.first_name = response.data.profile.first_name;
                this.second_name = response.data.profile.second_name;
                this.middle_name = response.data.profile.middle_name;
                this.birthday = response.data.profile.birthday;
                this.selectedRoles = response.data.userRoles;
              }).catch((error) => {
                console.log(error);
              })
              // this.username = '';
              // this.password = '';
              // this.confirm_password = '';
              // this.first_name = '';
              // this.second_name = '';
              // this.middle_name = '';
              // this.birthday = '';
              // this.email = '';
                // this.$axios({
                //     method: 'post',
                //     url: 'http://localhost:8080/backvuebato/profile'
                // }).then(response =>{
                //     // console.log( response );
                //     this.first_name = response.data.first_name;
                //     this.second_name = response.data.second_name;
                //     this.middle_name = response.data.middle_name;
                //     this.birthday = response.data.birthday;
                //     this.email = response.data.email;
                // }).catch((error) => {
                //     console.log(error);
                // })
                // this.$refs.observer.reset()
            },
        },
    }

</script>