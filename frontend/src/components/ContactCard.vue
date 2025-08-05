<template>
  <Card class="hover:shadow-lg transition-all duration-200 cursor-pointer group overflow-hidden" @click="viewContact">
    <div class="p-6 overflow-hidden">
      <div class="flex items-start space-x-4 overflow-hidden">
        <div class="flex-shrink-0">
          <Avatar class="h-16 w-16">
            <AvatarImage
              v-if="contact.thumbnail"
              :src="`data:image/jpeg;base64,${contact.thumbnail}`"
              :alt="contact.name"
            />
            <AvatarFallback class="bg-primary/10 text-primary">
              <User class="h-8 w-8" />
            </AvatarFallback>
          </Avatar>
        </div>
        
        <div class="flex-1 min-w-0">
          <div class="flex items-start justify-between">
            <div class="flex-1">
              <h3 class="text-lg font-semibold text-foreground truncate group-hover:text-primary transition-colors">
                {{ contact.name }}<span v-if="contact.nickname" class="text-muted-foreground font-normal"> ({{ contact.nickname }})</span>
              </h3>
              <div class="mt-2 space-y-1">
                <div class="flex items-center text-sm text-muted-foreground">
                  <Phone class="h-4 w-4 min-w-4 min-h-4 mr-2" />
                  {{ contact.phone }}
                </div>
                <div v-if="contact.email" class="flex items-center text-sm text-muted-foreground">
                  <Mail class="h-4 w-4 min-w-4 min-h-4 mr-2" />
                  <span :title="contact.email" style="max-width: 180px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; display: inline-block; vertical-align: bottom;">
                    {{ truncateEmail(contact.email) }}
                  </span>
                </div>
                <div v-if="contact.birthday" class="flex items-center text-sm text-muted-foreground">
                  <Calendar class="h-4 w-4 min-w-4 min-h-4 mr-2" />
                  {{ formatBirthday(contact.birthday) }}
                </div>
              </div>
            </div>
          </div>
          
          <div v-if="contact.description" class="mt-4 flex flex-wrap gap-2">
            <Badge variant="secondary" class="text-xs">
              {{ formatAge(contact.description.age) }}
            </Badge>
            <Badge variant="outline" class="text-xs">
              {{ formatSex(contact.description.sex) }}
            </Badge>
            <Badge variant="outline" class="text-xs">
              {{ formatHairColor(contact.description.hairColor) }}
            </Badge>
          </div>
        </div>
      </div>
    </div>
  </Card>
</template>

<script setup lang="ts">
import { User, Phone, Mail, Calendar } from 'lucide-vue-next'
import { useRouter } from 'vue-router'
import type { Contact } from '@/services/api'
import Card from '@/components/ui/card.vue'
import Avatar from '@/components/ui/avatar.vue'
import AvatarImage from '@/components/ui/avatar-image.vue'
import AvatarFallback from '@/components/ui/avatar-fallback.vue'
import Badge from '@/components/ui/badge.vue'

interface Props {
  contact: Contact
}

const props = defineProps<Props>()

const router = useRouter()

const viewContact = () => {
  router.push(`/contacts/${props.contact.id}`)
}

const formatAge = (age: string) => {
  const ageMap = {
    'CHILD': 'Child',
    'ADOLESCENT': 'Teen',
    'YOUNG_ADULT': 'Young Adult',
    'ADULT': 'Adult',
    'ELDER': 'Elder'
  }
  return ageMap[age as keyof typeof ageMap] || age
}

const formatSex = (sex: string) => {
  return sex === 'MALE' ? 'Male' : 'Female'
}

const formatHairColor = (hairColor: string) => {
  const colorMap = {
    'BLACK': 'Black Hair',
    'BROWN': 'Brown Hair',
    'BLONDE': 'Blonde Hair',
    'GINGER': 'Red Hair',
    'GRAY': 'Gray Hair',
    'WHITE': 'White Hair',
    'PAINTED': 'Colored Hair',
    'NONE': 'No Hair'
  }
  return colorMap[hairColor as keyof typeof colorMap] || hairColor
}

const truncateEmail = (email: string) => {
  if (!email) return ''
  return email.length > 30 ? email.slice(0, 27) + '...' : email
}

const formatBirthday = (birthday: string) => {
  if (!birthday) return ''
  // Try to format as YYYY-MM-DD or ISO string
  const date = new Date(birthday)
  if (!isNaN(date.getTime())) {
    return date.toLocaleDateString()
  }
  return birthday
}
</script>