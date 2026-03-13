/** @type {import('next').NextConfig} */
const nextConfig = {
  typescript: {
    ignoreBuildErrors: true,
  },
  images: {
    unoptimized: true,
  },
  turbopack: {
    root: __dirname,
  },
  i18n: {
    locales: ['en'],
    defaultLocale: 'en',
    localeDetection: false,
  },
}

export default nextConfig
