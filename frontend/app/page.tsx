"use client";

import { useState } from "react";
import { UrlShortenerForm } from "@/components/url-shortener-form";
import { UrlList } from "@/components/url-list";

export default function Home() {
  const [refreshTrigger, setRefreshTrigger] = useState(0);

  return (
    <div className="min-h-screen flex flex-col bg-background">

      <main className="flex-1">
        <section className="py-5 md:py-5">
          <div className="container mx-auto px-4">
            <div className="text-center mb-12">
              <h1 className="text-4xl md:text-6xl font-bold text-foreground mb-6 text-balance">
                Shorten your links,
                <br />
                <span className="text-primary">amplify your reach</span>
              </h1>
              <p className="text-lg text-muted-foreground max-w-2xl mx-auto mb-8 text-pretty">
                Transform long, unwieldy URLs into clean, shareable links. Track
                clicks, manage your links, and grow your audience.
              </p>
            </div>

            <UrlShortenerForm
              onUrlCreated={() => setRefreshTrigger((prev) => prev + 1)}
            />
          </div>
        </section>

        <section className="py-20">
          <div className="container mx-auto px-4">
            <UrlList refreshTrigger={refreshTrigger} />
          </div>
        </section>
      </main>
      
    </div>
  );
}
