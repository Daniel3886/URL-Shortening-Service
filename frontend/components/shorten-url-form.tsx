"use client"

import { useState } from "react"
import { InputButtonGroup } from "@/components/ui/input-button-group"
import { apiClient } from "@/lib/api"
import type { ApiError, CreateUrlRequest } from "@/lib/types"
import { OriginalUrlResult } from "./original-url-result"

type CreateResponse = {
  shortCode: string
}

export function ShortenUrlForm() {
  const [url, setUrl] = useState("")
  const [isLoading, setIsLoading] = useState(false)
  const [error, setError] = useState("")
  const [result, setResult] = useState<CreateResponse | null>(null)

  const handleSubmit = async () => {
    if (!url.trim()) return

    setIsLoading(true)
    setError("")
    setResult(null)
    
    try {
      const response = (await apiClient.createShortUrl({
        url: url.trim(),
      } as CreateUrlRequest))
      
      setResult(response)
      setUrl("")
    } catch (err) {
      const apiError = err as ApiError
      setError(apiError.message || "An error occurred")
    } finally {
      setIsLoading(false)
    }
  }

  return (
    <div>
      <InputButtonGroup
        value={url}
        onValueChange={setUrl}
        onSubmit={handleSubmit}
        disabled={isLoading}
        buttonLabel={isLoading ? "Shortening..." : "Shorten"}
      />

      {error ? (
        <p className="mt-3 text-left text-sm text-destructive">{error}</p>
      ) : null}

      {result ? (
        <OriginalUrlResult
          shortCode={result.shortCode}
          originalUrl={apiClient.getRedirectUrl(result.shortCode)}
        />
      ) : null}
    </div>
  )
}