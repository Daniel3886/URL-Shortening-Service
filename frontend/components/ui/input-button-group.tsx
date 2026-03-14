import { Button } from "@/components/ui/button"
import { Field } from "@/components/ui/field"
import { Input } from "@/components/ui/input"
import { Link2 } from "lucide-react"

export function InputButtonGroup() {
  return (
    <Field>
      <div className="flex w-full items-center gap-3 rounded-xl border border-border bg-muted/50 p-2 shadow-sm focus-within:ring-2 focus-within:ring-primary">
        <div className="relative flex-1">
          <Link2 className="pointer-events-none absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
          <Input
            id="input-button-group"
            placeholder="Paste your long URL here..."
            className="border-0 bg-transparent pl-10 shadow-none focus-visible:ring-0"
          />
        </div>
        <Button className="shrink-0 bg-primary px-4 text-primary-foreground hover:bg-primary/90">
          Shorten
        </Button>
      </div>
    </Field>
  )
}
