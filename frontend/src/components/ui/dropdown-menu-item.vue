<template>
  <DropdownMenuItem v-bind="$attrs" :class="[
    'flex items-center px-2 py-2 text-sm cursor-pointer select-none rounded-md transition-colors',
    $attrs['data-destructive']
      ? 'text-red-600 hover:bg-red-50 focus:bg-red-100 focus:text-red-700 font-semibold' // text red, bg on hover/focus
      : 'focus:bg-accent focus:text-accent-foreground',
    $attrs.class ?? ''
  ]">
    <slot />
  </DropdownMenuItem>
</template>

<script setup lang="ts">
import { DropdownMenuItem } from 'radix-vue'
</script>
