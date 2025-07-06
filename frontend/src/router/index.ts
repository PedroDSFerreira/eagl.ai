import { createRouter, createWebHistory } from 'vue-router'
import ContactList from '@/views/ContactList.vue'
import ContactDetail from '@/views/ContactDetail.vue'
import AddContact from '@/views/AddContact.vue'
import EditContact from '@/views/EditContact.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'ContactList',
      component: ContactList
    },
    {
      path: '/contacts/:id',
      name: 'ContactDetail',
      component: ContactDetail,
      props: true
    },
    {
      path: '/add',
      name: 'AddContact',
      component: AddContact
    },
    {
      path: '/edit/:id',
      name: 'EditContact',
      component: EditContact,
      props: true
    }
  ]
})

export default router