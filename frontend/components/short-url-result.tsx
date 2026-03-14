import { useState } from "react"
import { Button } from "@/components/ui/button"
import { Check, Copy } from "lucide-react"

type ShortUrlResultProps = {
  shortCode: string
  shortUrl: string
}

export function ShortUrlResult({ shortCode, shortUrl }: ShortUrlResultProps) {
  const [copied, setCopied] = useState(false)

  const handleCopy = async () => {
    await navigator.clipboard.writeText(shortUrl)
    setCopied(true)
    setTimeout(() => setCopied(false), 1200)
  }

  return (
    <div className="mt-4 rounded-xl border border-border bg-card p-4 text-left shadow-sm">
      <p className="text-sm text-muted-foreground">Your short link</p>

      <div className="mt-2 flex items-center justify-between gap-3">
        <a
          href={shortUrl}
          target="_blank"
          rel="noreferrer"
          className="block font-medium text-primary underline-offset-4 hover:underline break-all"
        >
          {shortUrl}
        </a>

        <Button type="button" size="sm" variant="outline" onClick={handleCopy}>
          {copied ? <Check className="mr-2 h-4 w-4" /> : <Copy className="mr-2 h-4 w-4" />}
          {copied ? "Copied" : "Copy"}
        </Button>
      </div>

      <p className="mt-2 text-sm text-muted-foreground">Code: {shortCode}</p>
    </div>
  )
}