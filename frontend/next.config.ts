import type { NextConfig } from "next";

const nextConfig: NextConfig = {
  images: {
    remotePatterns: [
       {
        hostname: "ik.imagekit.io",
        protocol: "https", 
       }
    ]
  },
  output: 'standalone',
};

export default nextConfig;

