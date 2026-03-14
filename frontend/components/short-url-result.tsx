type ShortUrlResultProps = {
  shortCode: string
  shortUrl: string
}

export function ShortUrlResult({ shortCode, shortUrl }: ShortUrlResultProps) {
  return (
    <div className="mt-4 rounded-xl border border-border bg-card p-4 text-left shadow-sm">
      <p className="text-sm text-muted-foreground">Your short link</p>
      <a
        href={shortUrl}
        target="_blank"
        rel="noreferrer"
        className="mt-1 block font-medium text-primary underline-offset-4 hover:underline"
      >
        {shortUrl}
      </a>
      <p className="mt-2 text-sm text-muted-foreground">Code: {shortCode}</p>
    </div>
  )
}