import { useState } from "react"
import { Button } from "@/components/ui/button"
import { AlertCircle, Check, Copy } from "lucide-react"

type OriginalUrlResultProps = {
  shortCode: string
  originalUrl: string
}

export function OriginalUrlResult({ shortCode, originalUrl }: OriginalUrlResultProps) {
  const [copied, setCopied] = useState(false)

  const handleCopy = async () => {
    await navigator.clipboard.writeText(originalUrl)
    setCopied(true)
    setTimeout(() => setCopied(false), 1200)
  }

  return (
    <div className="mt-4 rounded-xl border border-border bg-card p-4 text-left shadow-sm">
      <p className="text-sm text-muted-foreground">Original URL</p>

      <div className="mt-2 flex items-center justify-between gap-3">
        <a
          href={originalUrl}
          target="_blank"
          rel="noreferrer"
          className="block font-medium text-primary underline-offset-4 hover:underline break-all"
        >
          {originalUrl}
        </a>

        <Button type="button" size="sm" variant="outline" onClick={handleCopy}>
          {copied ? <Check className="mr-2 h-4 w-4" /> : <Copy className="mr-2 h-4 w-4" />}
          {copied ? "Copied" : "Copy"}
        </Button>
      </div>

      <p className="mt-2 text-sm text-muted-foreground">
        Short Code: <span className="font-mono">{shortCode}</span>
      </p>
      <div className="mt-3 flex items-start gap-2 rounded-lg bg-amber-50 p-3 border border-amber-200">
        <AlertCircle className="h-4 w-4 text-amber-600 mt-0.5 shrink-0" />
        <p className="text-sm text-amber-800">This code will expire in 24 hours</p>
      </div>
    </div>
  )
}