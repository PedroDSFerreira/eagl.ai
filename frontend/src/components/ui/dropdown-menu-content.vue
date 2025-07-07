<template>
  <DropdownMenuContent v-bind="$attrs" class="z-50 min-w-[8rem] overflow-hidden rounded-md border bg-popover p-1 text-popover-foreground shadow-md animate-in fade-in-0 zoom-in-95">
    <slot />
  </DropdownMenuContent>
</template>

<script setup lang="ts">
import { DropdownMenuContent } from 'radix-vue'
</script>
