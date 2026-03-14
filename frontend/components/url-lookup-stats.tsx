"use client"

import { useState } from "react"
import { InputInline } from "@/components/ui/inline"
import { OriginalUrlResult } from "@/components/url-result-stats"
import { apiClient } from "@/lib/api"

type LookupResponse = {
  shortCode?: string
  originalUrl?: string
  url?: string
}

export function OriginalUrlLookup() {
  const [shortCode, setShortCode] = useState("")
  const [isLoading, setIsLoading] = useState(false)
  const [error, setError] = useState("")
  const [result, setResult] = useState<{
    shortCode: string
    originalUrl: string
  } | null>(null)

  const handleLookup = async () => {
    if (!shortCode.trim()) return

    setIsLoading(true)
    setError("")
    setResult(null)

    try {
      const response = (await apiClient.getUrlStats(
        shortCode
      )) as LookupResponse

      console.log("Lookup response:", response)

      const originalUrl = response.originalUrl ?? response.url ?? ""

      setResult({
        shortCode: response.shortCode ?? shortCode,
        originalUrl,
      })
    } catch (err) {
      setError(err instanceof Error ? err.message : "Failed to fetch URL")
    } finally {
      setIsLoading(false)
    }
  }

  return (
    <div>
      <InputInline
        value={shortCode}
        onValueChange={setShortCode}
        onSubmit={handleLookup}
        disabled={isLoading}
      />

      {error ? (
        <p className="mt-3 text-left text-sm text-destructive">{error}</p>
      ) : null}

      {result ? <OriginalUrlResult {...result} /> : null}
    </div>
  )
}