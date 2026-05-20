---
name: Liao Lan Industrial
colors:
  surface: '#f9f9ff'
  surface-dim: '#d7dae5'
  surface-bright: '#f9f9ff'
  surface-container-lowest: '#ffffff'
  surface-container-low: '#f1f3ff'
  surface-container: '#ebedf9'
  surface-container-high: '#e5e8f3'
  surface-container-highest: '#dfe2ed'
  on-surface: '#181c23'
  on-surface-variant: '#434654'
  inverse-surface: '#2c3039'
  inverse-on-surface: '#eef0fc'
  outline: '#737686'
  outline-variant: '#c3c6d7'
  surface-tint: '#0353da'
  primary: '#003da6'
  on-primary: '#ffffff'
  primary-container: '#0052d9'
  on-primary-container: '#cbd6ff'
  inverse-primary: '#b4c5ff'
  secondary: '#5c5f60'
  on-secondary: '#ffffff'
  secondary-container: '#dee0e2'
  on-secondary-container: '#606365'
  tertiary: '#683b00'
  on-tertiary: '#ffffff'
  tertiary-container: '#895000'
  on-tertiary-container: '#ffcea0'
  error: '#ba1a1a'
  on-error: '#ffffff'
  error-container: '#ffdad6'
  on-error-container: '#93000a'
  primary-fixed: '#dbe1ff'
  primary-fixed-dim: '#b4c5ff'
  on-primary-fixed: '#00174b'
  on-primary-fixed-variant: '#003ea8'
  secondary-fixed: '#e1e2e4'
  secondary-fixed-dim: '#c5c6c8'
  on-secondary-fixed: '#191c1e'
  on-secondary-fixed-variant: '#444749'
  tertiary-fixed: '#ffdcbd'
  tertiary-fixed-dim: '#ffb86f'
  on-tertiary-fixed: '#2c1600'
  on-tertiary-fixed-variant: '#693c00'
  background: '#f9f9ff'
  on-background: '#181c23'
  surface-variant: '#dfe2ed'
typography:
  h1:
    fontFamily: Inter, PingFang SC
    fontSize: 24px
    fontWeight: '600'
    lineHeight: 32px
  h2:
    fontFamily: Inter, PingFang SC
    fontSize: 20px
    fontWeight: '600'
    lineHeight: 28px
  h3:
    fontFamily: Inter, PingFang SC
    fontSize: 16px
    fontWeight: '600'
    lineHeight: 24px
  body-lg:
    fontFamily: Inter, PingFang SC
    fontSize: 16px
    fontWeight: '400'
    lineHeight: 24px
  body-md:
    fontFamily: Inter, PingFang SC
    fontSize: 14px
    fontWeight: '400'
    lineHeight: 22px
  label-md:
    fontFamily: Inter, PingFang SC
    fontSize: 12px
    fontWeight: '500'
    lineHeight: 18px
    letterSpacing: 0.02em
  label-sm:
    fontFamily: Inter, PingFang SC
    fontSize: 10px
    fontWeight: '500'
    lineHeight: 14px
    letterSpacing: 0.04em
rounded:
  sm: 0.125rem
  DEFAULT: 0.25rem
  md: 0.375rem
  lg: 0.5rem
  xl: 0.75rem
  full: 9999px
spacing:
  container-margin: 16px
  stack-gap: 12px
  inline-gap: 8px
  section-padding: 24px
  grid-gutter: 12px
---

## Brand & Style

This design system is built on the pillars of engineering precision, structural reliability, and industrial efficiency. It is designed specifically for the "Liao Lan" (辽缆) brand to facilitate trust in high-stakes B2B and B2C transactions within the industrial cable sector. 

The visual language follows a **Corporate / Modern** aesthetic, prioritizing clarity and information density without sacrificing visual breathing room. It avoids decorative elements in favor of functional clarity, mimicking the orderly nature of high-end manufacturing. The goal is to evoke a sense of "Quality Certification" through every screen, ensuring users feel they are interacting with a top-tier state-adjacent industrial leader.

## Colors

The palette is anchored by **Deep Industrial Blue**, a color synonymous with technical expertise and corporate stability in the Chinese market. This is the primary driver for navigation, key actions, and brand identification.

**Professional Gray** serves as the structural foundation, used for page backgrounds and grouping sections to reduce eye strain during long procurement sessions. **Energetic Orange** is used sparingly and strategically; it is reserved exclusively for the most critical conversion points, such as "Add to Cart," "Buy Now," and "Contact Sales," ensuring these actions are immediately visible against the professional blue and gray backdrop. 

Neutrals are tiered to provide a clear hierarchy: dark charcoal for primary text, medium gray for secondary details, and light gray for borders and disabled states.

## Typography

The typography system utilizes **Inter** for alphanumeric characters paired with **PingFang SC** for Chinese script. This combination ensures a utilitarian and systematic feel that remains highly readable on mobile devices.

The hierarchy is strictly organized to handle complex technical data. Bold headlines denote major product categories and section starts, while body text uses a standard weight for specifications and descriptions. Label styles are used for technical parameters (e.g., voltage, cross-section area) and metadata. Line heights are slightly generous to prevent the dense technical specifications from feeling cramped, maintaining the "clean" industrial aesthetic.

## Layout & Spacing

This design system employs a **Fluid Grid** model optimized for the WeChat Mini-Program ecosystem. A standard 16px side margin ensures content does not crowd the edges of the mobile screen.

The layout rhythm is based on a 4px baseline, with most component gaps utilizing 8px or 12px increments. This tight spacing reflects the compact efficiency of industrial components. Section headers are given more vertical breathing room (24px) to clearly demarcate the transition between different technical data sets or product categories. Content cards use a 12px gutter to create a structured, organized product feed.

## Elevation & Depth

To maintain a professional and "grounded" feel, this design system avoids heavy shadows or floating elements. Instead, it utilizes **Low-contrast outlines** and **Tonal layers**.

Most content lives on white cards with a subtle 1px border (#E5E6EB). Depth is conveyed through background color changes—shifting from the main background (#F2F3F5) to pure white (#FFFFFF) for interactive elements. When a shadow is necessary (e.g., for a sticky bottom CTA bar), a very light, diffused ambient shadow with a 0.05 opacity is used to suggest a slight lift without appearing overly decorative or soft.

## Shapes

The shape language is **Soft (Level 1)**. Elements like buttons and cards feature a 4px (0.25rem) corner radius. This choice strikes a balance between the "sharp" corners of heavy machinery and the modern "rounded" UI expected by mobile users. 

Larger components like product category banners or modal overlays may use a 12px (rounded-xl) radius to feel more approachable, but the core transactional elements remain crisp and professional.

## Components

### Buttons
Primary buttons use the Deep Industrial Blue with white text. The "Buy" and "Checkout" actions use the Energetic Orange. All buttons have a fixed height of 44px or 48px to ensure a comfortable touch target for industrial workers or buyers on the move.

### Cards
Cards are the primary container. They feature a white background, a 1px light gray border, and 4px rounded corners. In product listings, cards should display a clear image of the cable cross-section, the product name in H3, and key specs in Label-md.

### Chips & Tags
Technical tags (e.g., "Flame Retardant," "In Stock") use a light blue tint background with Deep Blue text. They are small, 24px in height, and help buyers filter through large inventories quickly.

### Section Headers
Headers must be distinct, featuring a 4px vertical blue line to the left of the title text. This is a common pattern in Chinese corporate design that reinforces the brand's primary color and provides a clear visual anchor.

### Input Fields
Inputs are rectangular with a 1px border. Focus states are indicated by a 1px solid Deep Blue border and a very faint blue glow. This provides clear feedback during the entry of complex order quantities or specifications.