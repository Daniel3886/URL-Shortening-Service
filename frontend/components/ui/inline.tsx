import { Button } from "@/components/ui/button"
import { Field } from "@/components/ui/field"
import { Input } from "@/components/ui/input"
import { Search } from "lucide-react"

type InputInlineProps = {
  value: string
  onValueChange: (value: string) => void
  onSubmit: () => void
  disabled?: boolean
}

export function InputInline({
  value,
  onValueChange,
  onSubmit,
  disabled = false,
}: InputInlineProps) {
  return (
    <Field orientation="horizontal">
      <div className="relative flex-1">
        <Search className="pointer-events-none absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
        <Input
          type="search"
          value={value}
          onChange={(e) => onValueChange(e.target.value)}
          onKeyDown={(e) => {
            if (e.key === "Enter") onSubmit()
          }}
          placeholder="Enter a short code for original URL lookup..."
          className="pl-10"
          disabled={disabled}
        />
      </div>
      <Button type="button" onClick={onSubmit} disabled={disabled}>
        Lookup
      </Button>
    </Field>
  )
}
