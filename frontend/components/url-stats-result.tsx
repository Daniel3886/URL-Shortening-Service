"use client"

import { useState } from "react"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Check, Copy, Edit3, Save, Trash2, X } from "lucide-react"
import { apiClient } from "@/lib/api"

type UrlResultStatsProps = {
  shortCode: string
  url: string
  onEdit?: (shortCode: string) => void
  onDelete?: (shortCode: string) => void
}

export function UrlResultStats({
  shortCode,
  url,
  onEdit,
  onDelete,
}: UrlResultStatsProps) {
  const [copied, setCopied] = useState(false)
  const [currentUrl, setCurrentUrl] = useState(url)

  const [isEditing, setIsEditing] = useState(false)
  const [editUrl, setEditUrl] = useState(url)

  const [isUpdating, setIsUpdating] = useState(false)
  const [isDeleting, setIsDeleting] = useState(false)

  const [statusMessage, setStatusMessage] = useState("")
  const [errorMessage, setErrorMessage] = useState("")
  const [isRemoved, setIsRemoved] = useState(false)

  const normalizedUrl =
    currentUrl.startsWith("http://") || currentUrl.startsWith("https://")
      ? currentUrl
      : `http://${currentUrl}`

  const handleCopy = async () => {
    try {
      await navigator.clipboard.writeText(currentUrl)
      setCopied(true)
      setTimeout(() => setCopied(false), 2000)
    } catch {
      setErrorMessage("Failed to copy URL")
    }
  }

  const handleStartEdit = () => {
    if (isRemoved) return
    setEditUrl(currentUrl)
    setIsEditing(true)
    setErrorMessage("")
    setStatusMessage("")
  }

  const handleCancelEdit = () => {
    setIsEditing(false)
    setEditUrl(currentUrl)
  }

  const handleUpdate = async () => {
    if (!editUrl.trim() || editUrl.trim() === currentUrl) {
      setIsEditing(false)
      return
    }

    setIsUpdating(true)
    setErrorMessage("")
    setStatusMessage("")

    try {
      await apiClient.updateShortUrl(shortCode, { url: editUrl.trim() })
      setCurrentUrl(editUrl.trim())
      setStatusMessage("URL updated successfully.")
      setIsEditing(false)
      onEdit?.(shortCode)
    } catch (err) {
      setErrorMessage(err instanceof Error ? err.message : "Failed to update URL")
    } finally {
      setIsUpdating(false)
    }
  }

  const handleDelete = async () => {
    if (isRemoved) return

    setIsDeleting(true)
    setErrorMessage("")
    setStatusMessage("")

    try {
      await apiClient.deleteShortUrl(shortCode)
      setIsRemoved(true)
      setStatusMessage("URL removed successfully.")
      onDelete?.(shortCode)
    } catch (err) {
      setErrorMessage(err instanceof Error ? err.message : "Failed to delete URL")
    } finally {
      setIsDeleting(false)
    }
  }

  return (
    <div className="mt-4 rounded-xl border border-border bg-card p-4 text-left shadow-sm">
      <p className="text-sm text-muted-foreground">Short URL for {shortCode}</p>

      <div className="mt-2 flex items-center justify-between gap-3">
        {isEditing ? (
          <div className="flex min-w-0 flex-1 items-center gap-2">
            <Input
              type="text"
              value={editUrl}
              onChange={(e) => setEditUrl(e.target.value)}
              className="h-8 text-xs"
              disabled={isUpdating}
            />
            <Button
              type="button"
              size="icon"
              variant="ghost"
              onClick={handleUpdate}
              disabled={isUpdating}
              className="h-8 w-8"
            >
              <Save className="h-4 w-4" />
            </Button>
            <Button
              type="button"
              size="icon"
              variant="ghost"
              onClick={handleCancelEdit}
              disabled={isUpdating}
              className="h-8 w-8"
            >
              <X className="h-4 w-4" />
            </Button>
          </div>
        ) : (
          <>
            <a
              href={normalizedUrl}
              target="_blank"
              rel="noreferrer"
              className={`block break-all font-medium text-primary underline-offset-4 hover:underline ${
                isRemoved ? "pointer-events-none opacity-50" : ""
              }`}
            >
              {currentUrl}
            </a>

            <div className="flex items-center gap-2">
              <Button
                type="button"
                size="icon"
                variant="ghost"
                onClick={handleCopy}
                disabled={isDeleting || isRemoved}
                className="h-8 w-8"
              >
                {copied ? (
                  <Check className="h-4 w-4 text-green-600" />
                ) : (
                  <Copy className="h-4 w-4" />
                )}
              </Button>

              <Button
                type="button"
                size="icon"
                variant="ghost"
                onClick={handleStartEdit}
                disabled={isDeleting || isRemoved}
                className="h-8 w-8"
              >
                <Edit3 className="h-4 w-4" />
              </Button>

              <Button
                type="button"
                size="icon"
                variant="ghost"
                onClick={handleDelete}
                disabled={isDeleting || isRemoved}
                className="h-8 w-8 text-destructive hover:text-destructive"
              >
                <Trash2 className="h-4 w-4" />
              </Button>
            </div>
          </>
        )}
      </div>

      {statusMessage ? <p className="mt-2 text-sm text-green-600">{statusMessage}</p> : null}
      {errorMessage ? <p className="mt-2 text-sm text-destructive">{errorMessage}</p> : null}
    </div>
  )
}

