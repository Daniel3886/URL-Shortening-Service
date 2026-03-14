import { ShortenUrlForm } from './shorten-url-form'
import { UrlLookupStats } from './url-lookup-stats'

export default function HeroSection() {
    return (
        <>
            <main className="flex flex-col items-center justify-center px-6 pt-12 pb-16 text-center">
                <h1 className="max-w-3xl text-5xl font-bold tracking-tight md:text-6xl">
                    Shorten your links,{' '}
                    <span className="text-primary">amplify your reach</span>
                </h1>

                <p className="mt-6 max-w-xl text-lg text-muted-foreground">
                    Transform long, unwieldy URLs into clean, shareable links.
                    Track clicks, manage your links, and grow your audience.
                </p>

                <div className="mt-10 w-full max-w-2xl">
                    <ShortenUrlForm />
                </div>
            </main>

            <section className="px-6 pb-16">
                <div className="mx-auto max-w-2xl">
                    <h2 className="mb-6 text-left text-xl font-semibold text-foreground">
                        Your Links
                    </h2>

                    <UrlLookupStats />
                </div>
            </section>
        </>
    )
}
