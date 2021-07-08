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
                    Edit user
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

                <v-spacer></v-spacer>
                <v-spacer></v-spacer>

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
                            Change Pass
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
                                            v-model = "pass"
                                            :rules="[rules.required, rules.min]"
                                            label="New Password*"
                                            hint="At least 8 characters"
                                            required
                                        ></v-text-field>
                                    </v-col>
                                    <v-col cols="12">
                                        <v-text-field
                                            v-model = "pass_confirm"
                                            :rules="[rules.required, rules.min]"
                                            label="Confirm New Password*"
                                            hint="At least 8 characters"
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
                                @click="closeChangePass"
                            >
                                Close
                            </v-btn>
                            <v-btn
                                color="blue darken-1"
                                text
                                @click="changePass"
                            >
                                Save
                            </v-btn>
                        </v-card-actions>
                    </v-card>
                </v-dialog>

                <v-dialog
                    v-model="editAlert"
                    transition="dialog-top-transition"
                    max-width="600"
                >
                    <template v-slot:activator="{ on, attrs }">
                        <!--                <v-btn-->
                        <!--                    color="primary"-->
                        <!--                    v-bind="attrs"-->
                        <!--                    v-on="on"-->
                        <!--                >From the top-->
                        <!--                </v-btn>-->
                    </template>
                    <template v-slot:default="dialog">
                        <v-card>
                            <v-toolbar
                                color="primary"
                                dark
                            >Edit user alert
                            </v-toolbar>
                            <v-card-text>
                                <div class="text-h2 pa-12"> username already taken </div>
                            </v-card-text>
                            <v-card-actions class="justify-end">
                                <v-btn
                                    text
                                    @click="editAlert = false"
                                >Close
                                </v-btn>
                            </v-card-actions>
                        </v-card>
                    </template>
                </v-dialog>

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
    data() {
        return {
            username: '',
            first_name: '',
            second_name: '',
            middle_name: '',
            birthday: '',
            name: '',
            email: '',
            select: null,
            roles: [],
            selectedRoles: [],
            dialog: false,
            pass: '',
            pass_confirm: '',
            rules: {
                required: value => !!value || 'Required.',
                min: v => v.length >= 8 || 'Min 8 characters',
                emailMatch: () => (`The email and password you entered don't match`),
            },
            editAlert: false,
            errors: '',
        }
    },
    mounted() {
        console.log(this.$route.params);
        let person = this.$route.params;
        // TODO reload page removes params
        // for now just return back to Persons after refresh page
        if (Object.keys(person).length === 0 && person.constructor === Object) {
            router.push({name: 'Persons'});
        }
        this.$axios({
            method: 'post',
            url: this.$api_url + 'editUser',
            data: person
        }).then(response => {
            console.log(response);
            this.roles = response.data.roles;
            this.username = response.data.profile.username;
            this.first_name = response.data.profile.first_name;
            this.second_name = response.data.profile.second_name;
            this.middle_name = response.data.profile.middle_name;
            this.birthday = response.data.profile.birthday;
            this.email = response.data.profile.email;
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
                formValues["first_name"] = this.first_name;
                formValues["second_name"] = this.second_name;
                formValues["middle_name"] = this.middle_name;
                formValues["birthday"] = this.birthday;
                formValues["email"] = this.email;
                formValues["roles"] = this.selectedRoles;
                console.log(this.$route.params);
                let person = this.$route.params;
                // If on the moment of editing user no user in route.params, for now just return back
                if (Object.keys(person).length === 0 && person.constructor === Object) {
                  router.push({name: 'Persons'});
                }
                formValues["person"] = person;
                this.$axios({
                    method: 'post',
                    url: this.$api_url + 'doEditUser',
                    // headers: {},
                    data: formValues
                }).then(response => {
                    console.log(response);
                    // router.push({ name: 'Persons' });
                    this.username = response.data.username;
                    this.first_name = response.data.first_name;
                    this.second_name = response.data.second_name;
                    this.middle_name = response.data.middle_name;
                    this.birthday = response.data.birthday;
                    this.email = response.data.email;
                    if(null != response.data.errors){
                        // TODO show modal with errors
                        console.log(response.data.errors);
                        this.editAlert = true;
                    }
                }).catch((error) => {
                    console.log(error);
                })
            }
        },
        clear() {
            // TODO reload page removes params
            console.log(this.$route.params);
            let person = this.$route.params;
            this.$axios({
                method: 'post',
                url: this.$api_url + 'editUser',
                data: person
            }).then(response => {
                console.log(response);
                this.roles = response.data.roles;
                this.username = response.data.profile.username;
                this.first_name = response.data.profile.first_name;
                this.second_name = response.data.profile.second_name;
                this.middle_name = response.data.profile.middle_name;
                this.birthday = response.data.profile.birthday;
                this.email = response.data.profile.email;
                this.selectedRoles = response.data.userRoles;
            }).catch((error) => {
                console.log(error);
            })
        },
        changePass() {
            if( this.pass === '' || this.pass !== this.pass_confirm){
                console.log('Password and Confirm Password should match ')
                return;
            }
            this.dialog = false
            let person = this.$route.params;
            person["pass"] = this.pass;
            this.$axios({
                method: 'post',
                url: this.$api_url + 'changePass',
                data: person
            }).then(response => {
                console.log(response);
                this.roles = response.data.roles;
                this.username = response.data.profile.username;
                this.first_name = response.data.profile.first_name;
                this.second_name = response.data.profile.second_name;
                this.middle_name = response.data.profile.middle_name;
                this.birthday = response.data.profile.birthday;
                this.email = response.data.profile.email;
                this.selectedRoles = response.data.userRoles;
            }).catch((error) => {
                console.log(error);
            })
            this.pass = '';
            this.pass_confirm = '';
        },
        closeChangePass(){
            this.dialog = false;
            this.pass = '';
            this.pass_confirm = '';
        },
    },
}

</script>