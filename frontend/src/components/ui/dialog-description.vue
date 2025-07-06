<template>
  <DialogDescription :class="cn('text-sm text-muted-foreground', $attrs.class)">
    <slot />
  </DialogDescription>
</template>

<script setup lang="ts">
import { DialogDescription } from 'radix-vue'
import { cn } from '@/lib/utils'
</script>