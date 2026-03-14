import { Button } from "@/components/ui/button"
import { Field } from "@/components/ui/field"
import { Input } from "@/components/ui/input"
import { LinkIcon, Search } from "lucide-react"

export function InputInline() {
  return (
    <Field orientation="horizontal">
      <div className="relative flex-1">
        <LinkIcon className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-muted-foreground" />
        <Input
          type="search"
          placeholder="Enter a short url for original url lookup..."
          className="pl-10"
        />
      </div>
      <Button>Lookup</Button>
    </Field>
  )
}
