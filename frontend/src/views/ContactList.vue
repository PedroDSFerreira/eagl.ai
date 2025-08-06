<template>
  <div class="container mx-auto px-4 py-8">
    <div class="mb-8">
      <h1 class="text-3xl font-bold tracking-tight">Contacts</h1>
      <p class="text-muted-foreground mt-2">Manage your contact list easily.</p>
      <div class="mt-6 w-full max-w-3xl mx-auto">
        <div class="flex flex-col gap-4">
          <!-- Search bar -->
          <div class="flex items-center gap-2 min-w-0">
            <Input v-model="searchName" placeholder="Find a contact..." class="w-full text-base" @keyup.enter="onSearch" />
            <Button @click="onSearch" variant="default" class="px-4 py-2 whitespace-nowrap">Search</Button>
          </div>
        </div>
        <!-- Active filters row and Add filter button -->
        <div class="flex flex-wrap items-center gap-2 mt-4">
          <template v-for="(filter, idx) in activeFilters" :key="idx">
            <button
              @click="removeFilter(idx)"
              type="button"
              class="rounded-full border px-2 py-0.5 text-[11px] font-semibold transition-colors focus:ring-offset-2 border-transparent bg-secondary text-secondary-foreground hover:bg-secondary/80 flex items-center gap-1 cursor-pointer focus:ring-2 focus:ring-secondary focus:outline-none"
            >
              {{ filterLabels[filter.type] }}: {{ filterOptions[filter.type][filter.value] || filter.value }}
              <svg xmlns="http://www.w3.org/2000/svg" class="inline h-4 w-4 ml-1" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" /></svg>
            </button>
          </template>
          <!-- Add filter button below search bar and next to filters -->
          <div v-if="availableFilterTypes.length > 0" class="relative">
            <button ref="addFilterBtn" @click="toggleFilterPopup" type="button" class="rounded-full border px-2 py-0.5 text-[11px] font-semibold transition-colors focus:ring-offset-2 border-transparent bg-primary/10 text-primary hover:bg-primary/20 flex items-center gap-1 cursor-pointer focus:ring-2 focus:ring-primary focus:outline-none">
              <svg xmlns="http://www.w3.org/2000/svg" class="inline h-4 w-4 mr-1" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" /></svg>Add filter
            </button>
            <!-- Desktop popup -->
            <transition name="fade">
              <div
                v-if="showFilterSelect && !isMobile.value"
                ref="filterPopup"
                class="absolute left-0 z-30 mt-2 w-80 bg-background border border-border rounded-xl shadow-2xl p-5 flex flex-col gap-4 animate-fade-in"
                style="top:100%"
              >
                <div class="flex flex-col gap-2">
                  <label for="filter-type" class="text-xs font-semibold text-muted-foreground mb-1">Filter type</label>
                  <select
                    id="filter-type"
                    v-model="newFilterType"
                    class="border border-input rounded-lg px-3 py-2 bg-background text-foreground w-full focus:ring-2 focus:ring-primary focus:outline-none transition"
                  >
                    <option value="" disabled selected hidden>Select filter...</option>
                    <option v-for="key in availableFilterTypes" :key="key" :value="key">{{ filterLabels[key] }}</option>
                  </select>
                </div>
                <div class="flex flex-col gap-2" v-if="newFilterType && filterOptions[newFilterType]">
                  <label for="filter-value" class="text-xs font-semibold text-muted-foreground mb-1">Value</label>
                  <select
                    id="filter-value"
                    v-model="newFilterValue"
                    class="border border-input rounded-lg px-3 py-2 bg-background text-foreground w-full focus:ring-2 focus:ring-primary focus:outline-none transition"
                  >
                    <option value="" disabled selected hidden>Select value</option>
                    <option v-for="(label, val) in filterOptions[newFilterType]" :key="val" :value="val">{{ label }}</option>
                  </select>
                </div>
              </div>
            </transition>
            <!-- Mobile full-screen modal -->
            <transition name="fade">
              <div
                v-if="showMobileFilterModal && isMobile"
                class="fixed inset-0 z-50 bg-black/40 flex items-center justify-center animate-fade-in"
                @click="onMobileModalBgClick"
              >
                <div class="bg-background rounded-xl shadow-2xl w-full max-w-xs mx-auto p-6 flex flex-col gap-4 relative" @click.stop>
                  <button @click="closeMobileFilterModal" class="absolute top-3 right-3 text-2xl px-2 py-1 rounded-full hover:bg-muted focus:outline-none">&times;</button>
                  <h2 class="text-lg font-bold text-center mb-2">Add Filter</h2>
                  <div class="flex flex-col gap-2">
                    <label for="mobile-filter-type" class="text-xs font-semibold text-muted-foreground mb-1">Filter type</label>
                    <select
                      id="mobile-filter-type"
                      v-model="newFilterType"
                      class="border border-input rounded-lg px-3 py-2 bg-background text-foreground w-full focus:ring-2 focus:ring-primary focus:outline-none transition"
                    >
                      <option value="" disabled selected hidden>Select filter...</option>
                      <option v-for="key in availableFilterTypes" :key="key" :value="key">{{ filterLabels[key] }}</option>
                    </select>
                  </div>
                  <div class="flex flex-col gap-2" v-if="newFilterType && filterOptions[newFilterType]">
                    <label for="mobile-filter-value" class="text-xs font-semibold text-muted-foreground mb-1">Value</label>
                    <select
                      id="mobile-filter-value"
                      v-model="newFilterValue"
                      class="border border-input rounded-lg px-3 py-2 bg-background text-foreground w-full focus:ring-2 focus:ring-primary focus:outline-none transition"
                    >
                      <option value="" disabled selected hidden>Select value</option>
                      <option v-for="(label, val) in filterOptions[newFilterType]" :key="val" :value="val">{{ label }}</option>
                    </select>
                  </div>
                </div>
              </div>
            </transition>
          </div>
        </div>
      </div>
    </div>
    
    <div v-if="loading" class="flex justify-center items-center py-12">
      <Loader2 class="h-8 w-8 animate-spin" />
    </div>
    
    <div v-else-if="error" class="text-center py-12">
      <AlertTriangle class="h-12 w-12 text-destructive mx-auto mb-4" />
      <h3 class="text-lg font-semibold mb-2">Error loading contacts</h3>
      <p class="text-muted-foreground mb-4">{{ error }}</p>
      <Button @click="fetchContacts">Try Again</Button>
    </div>
    
    <div v-else-if="contacts.length === 0" class="text-center py-12">
      <Users class="h-12 w-12 text-muted-foreground mx-auto mb-4" />
      <h3 class="text-lg font-semibold mb-2">No contacts</h3>
      <p class="text-muted-foreground mb-4">Get started by creating your first contact.</p>
      <Button as-child>
        <router-link to="/add" class="flex items-center space-x-2">
          <Plus class="h-4 w-4" />
          <span>Add Contact</span>
        </router-link>
      </Button>
    </div>
    
    <div v-else>
      <div :class="gridColsClass + ' gap-6 justify-center'">
        <ContactCard
          v-for="contact in contacts"
          :key="contact.id"
          :contact="contact"
        />
      </div>
      <div v-if="totalPages > 1" class="flex justify-center mt-8">
        <div class="flex items-center gap-2 px-4 py-2 rounded-full shadow bg-background border border-border">
          <button
            @click="prevPage"
            :disabled="currentPage === 1"
            class="flex items-center px-3 py-1 rounded-full text-primary hover:bg-primary/10 disabled:opacity-40 disabled:cursor-not-allowed transition border border-transparent focus:ring-2 focus:ring-primary focus:outline-none"
            aria-label="Previous page"
          >
            <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7" /></svg>
          </button>
          <span class="text-sm font-medium text-foreground select-none px-2">
            Page <span class="font-bold">{{ currentPage }}</span> of <span class="font-bold">{{ totalPages }}</span>
          </span>
          <button
            @click="nextPage"
            :disabled="currentPage === totalPages"
            class="flex items-center px-3 py-1 rounded-full text-primary hover:bg-primary/10 disabled:opacity-40 disabled:cursor-not-allowed transition border border-transparent focus:ring-2 focus:ring-primary focus:outline-none"
            aria-label="Next page"
          >
            <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7" /></svg>
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
// Close mobile modal when clicking outside modal content
function onMobileModalBgClick(e: MouseEvent) {
  // Only close if the click is on the background (not on modal content)
  if (e.target === e.currentTarget) {
    closeMobileFilterModal()
  }
}
import { computed } from 'vue'
// Compute available filter types (not already selected)
const availableFilterTypes = computed(() => {
  return Object.keys(filterLabels).filter(type => !activeFilters.value.some(f => f.type === type))
})

// Dynamic grid columns class based on number of contacts
const gridColsClass = computed(() => {
  const count = contacts.value.length
  if (count === 1) return 'grid grid-cols-1'
  if (count === 2) return 'grid grid-cols-1 md:grid-cols-2'
  if (count >= 3) return 'grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3'
  return 'grid grid-cols-1'
})

import { ref, onMounted, onUnmounted, watch } from 'vue'
// Mobile detection and modal state
const isMobile = ref(false)
const showMobileFilterModal = ref(false)

function updateIsMobile() {
  isMobile.value = window.matchMedia('(max-width: 640px)').matches
}

function closeMobileFilterModal() {
  showMobileFilterModal.value = false
  newFilterType.value = ''
  newFilterValue.value = ''
}
import { Users, Plus, AlertTriangle, Loader2 } from 'lucide-vue-next'
import { useToast } from 'vue-toastification'
import { contactsApi, type Contact } from '@/services/api'
import ContactCard from '@/components/ContactCard.vue'
import Button from '@/components/ui/button.vue'
import Input from '@/components/ui/input.vue'

const showFilterSelect = ref(false)
const addFilterBtn = ref<HTMLElement | null>(null)
const filterPopup = ref<HTMLElement | null>(null)

function toggleFilterPopup() {
  if (isMobile.value) {
    showMobileFilterModal.value = true
  } else {
    showFilterSelect.value = !showFilterSelect.value
  }
}

function closeFilterPopup(e: MouseEvent) {
  if (!showFilterSelect.value) return
  const popup = filterPopup.value
  const btn = addFilterBtn.value
  if (popup && !popup.contains(e.target as Node) && btn && !btn.contains(e.target as Node)) {
    showFilterSelect.value = false
  }
}

onMounted(() => {
  updateIsMobile()
  window.addEventListener('resize', updateIsMobile)
  window.addEventListener('mousedown', closeFilterPopup)
})
onUnmounted(() => {
  window.removeEventListener('resize', updateIsMobile)
  window.removeEventListener('mousedown', closeFilterPopup)
})

const contacts = ref<Contact[]>([])
const loading = ref(true)
const error = ref<string | null>(null)
const toast = useToast()
const searchName = ref('')
const activeFilters = ref<{ type: string, value: string }[]>([])
const newFilterType = ref('')
const newFilterValue = ref('')
const currentPage = ref(1)
const pageSize = ref(9)
const totalPages = ref(1)

const filterLabels: Record<string, string> = {
  age: 'Age',
  sex: 'Sex',
  skinTone: 'Skin Tone',
  eyeColor: 'Eye Color',
  hairType: 'Hair Type',
  hairColor: 'Hair Color',
  facialHair: 'Facial Hair'
}
const filterOptions: Record<string, Record<string, string>> = {
  age: {
    CHILD: 'Child',
    ADOLESCENT: 'Adolescent',
    YOUNG_ADULT: 'Young Adult',
    ADULT: 'Adult',
    ELDER: 'Elder'
  },
  sex: {
    MALE: 'Male',
    FEMALE: 'Female'
  },
  skinTone: {
    DARK: 'Dark',
    MEDIUM: 'Medium',
    LIGHT: 'Light'
  },
  eyeColor: {
    BROWN: 'Brown',
    GREEN: 'Green',
    BLUE: 'Blue'
  },
  hairType: {
    LONG: 'Long',
    MEDIUM: 'Medium',
    SHORT: 'Short',
    NONE: 'None'
  },
  hairColor: {
    BLACK: 'Black',
    BROWN: 'Brown',
    GRAY: 'Gray',
    WHITE: 'White',
    BLONDE: 'Blonde',
    GINGER: 'Ginger',
    PAINTED: 'Painted',
    NONE: 'None'
  },
  facialHair: {
    true: 'Yes',
    false: 'No'
  }
}

const fetchContacts = async () => {
  try {
    loading.value = true
    error.value = null
    const params: Record<string, any> = {
      page: currentPage.value - 1,
      size: pageSize.value
    }
    if (searchName.value) params.name = searchName.value
    activeFilters.value.forEach(f => {
      params[f.type] = f.value
    })
    const response = await contactsApi.getContacts(params)
    if (response.data.content) {
      contacts.value = response.data.content
      totalPages.value = response.data.totalPages || 1
    } else {
      contacts.value = response.data
      totalPages.value = 1
    }
  } catch (err: any) {
    console.error('Error fetching contacts:', err)
    error.value = err.response?.data?.message || 'Failed to load contacts'
    toast.error('Failed to load contacts')
  } finally {
    loading.value = false
  }
}

const onSearch = () => {
  currentPage.value = 1
  fetchContacts()
}

watch([newFilterType, newFilterValue], ([type, value]) => {
  if (type && value) {
    // Prevent duplicate filters of same type
    const idx = activeFilters.value.findIndex(f => f.type === type)
    if (idx !== -1) activeFilters.value.splice(idx, 1)
    activeFilters.value.push({ type, value })
    newFilterType.value = ''
    newFilterValue.value = ''
    currentPage.value = 1
    fetchContacts()
    showFilterSelect.value = false
    if (isMobile.value) {
      showMobileFilterModal.value = false
    }
  }
})

const removeFilter = (idx: number) => {
  activeFilters.value.splice(idx, 1)
  currentPage.value = 1
  fetchContacts()
}

const prevPage = () => {
  if (currentPage.value > 1) {
    currentPage.value--
    fetchContacts()
  }
}
const nextPage = () => {
  if (currentPage.value < totalPages.value) {
    currentPage.value++
    fetchContacts()
  }
}



onMounted(() => {
  fetchContacts()
})
</script>