<template>
  <Primitive
    :as="as"
    :as-child="asChild"
    :class="cn(
      'text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70',
      $attrs.class
    )"
  >
    <slot />
  </Primitive>
</template>

<script setup lang="ts">
import { Primitive } from 'radix-vue'
import type { PrimitiveProps } from 'radix-vue'
import { cn } from '@/lib/utils'

interface Props extends PrimitiveProps {}

const props = withDefaults(defineProps<Props>(), {
  as: 'label',
})
</script>