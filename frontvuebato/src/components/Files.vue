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
    </v-card>
</template>
<script>
export default {
    data() {
        return {
            file: null,
        }
    },
    methods: {
        submit(){
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
        }
    }
}
</script>