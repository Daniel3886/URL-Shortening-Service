type OriginalUrlResultProps = {
  shortCode: string
  originalUrl: string
}

export function OriginalUrlResult({
  shortCode,
  originalUrl,
}: OriginalUrlResultProps) {
  return (
    <div className="mt-4 rounded-xl border border-border bg-card p-4 text-left shadow-sm">
      <p className="text-sm text-muted-foreground">Original URL for {shortCode}</p>
      <a
        href={originalUrl}
        target="_blank"
        rel="noreferrer"
        className="mt-1 block font-medium text-primary underline-offset-4 hover:underline break-all"
      >
        {originalUrl}
      </a>
    </div>
  )
}