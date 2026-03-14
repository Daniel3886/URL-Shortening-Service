import { Button } from "@/components/ui/button"
import { Field } from "@/components/ui/field"
import { Input } from "@/components/ui/input"
import { Link2 } from "lucide-react"

type InputButtonGroupProps = {
  value: string
  onValueChange: (value: string) => void
  onSubmit: () => void
  disabled?: boolean
  buttonLabel?: string
}

export function InputButtonGroup({
  value,
  onValueChange,
  onSubmit,
  disabled = false,
  buttonLabel = "Shorten",
}: InputButtonGroupProps) {
  return (
    <Field>
      <div className="flex w-full items-center gap-3 rounded-xl border border-border bg-muted/50 p-2 shadow-sm focus-within:ring-2 focus-within:ring-primary">
        <div className="relative flex-1">
          <Link2 className="pointer-events-none absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
          <Input
            id="input-button-group"
            value={value}
            onChange={(e) => onValueChange(e.target.value)}
            onKeyDown={(e) => {
              if (e.key === "Enter") onSubmit()
            }}
            placeholder="Paste your long URL here..."
            className="border-0 bg-transparent pl-10 shadow-none focus-visible:ring-0"
            disabled={disabled}
          />
        </div>
        <Button
          type="button"
          onClick={onSubmit}
          disabled={disabled}
          className="shrink-0 bg-primary px-4 text-primary-foreground hover:bg-primary/90"
        >
          {buttonLabel}
        </Button>
      </div>
    </Field>
  )
}
