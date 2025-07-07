import { ref, onMounted, watch } from 'vue'

const theme = ref<'light' | 'dark'>('light')

function setTheme(newTheme: 'light' | 'dark') {
  theme.value = newTheme
  document.documentElement.classList.toggle('dark', newTheme === 'dark')
  localStorage.setItem('theme', newTheme)
}

function toggleTheme() {
  setTheme(theme.value === 'dark' ? 'light' : 'dark')
}

onMounted(() => {
  const saved = localStorage.getItem('theme') as 'light' | 'dark' | null
  if (saved) setTheme(saved)
  else setTheme(document.documentElement.classList.contains('dark') ? 'dark' : 'light')
})

// Listen for changes to the document class (e.g., system theme changes)
if (typeof window !== 'undefined') {
  const observer = new MutationObserver(() => {
    theme.value = document.documentElement.classList.contains('dark') ? 'dark' : 'light'
  })
  observer.observe(document.documentElement, { attributes: true, attributeFilter: ['class'] })
}

export function useTheme() {
  return { theme, setTheme, toggleTheme }
}
