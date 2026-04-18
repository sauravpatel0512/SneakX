This is a documentation/spec request, not a code change — here's the full design spec for SneakX in Jetpack Compose / Material 3, derived from the current web prototype so v

S
lime accent, bold display type with occasional italic serif. All values ​​b

0.
🎨
Token	Hex	Use
primary	#1A1A22	Primary buttons, bottom nav background, top text, st
onPrimary	#FAFAF7	Text/i
primaryContainer	#2A2A33	Ho
onPrimaryContainer	#FAFAF7	T
secondary (accent)	#D4FF3A	L
onSecondary	#1A1A22	T
background	#F8F6F1	HAS
onBackground	#1A1A22	D
surface	#FFFFFF	C
surfaceVariant	#F1EEE7	S
onSurface	#1A1A22	That
onSurfaceVariant	#6B6B73	S
outline	#E2DED5	1
outlineVariant	#EDEAE3	Fa
error	#C8372D	Val
onError	#FAFAF7	You
success	#3A8A5C	"
Where each color goes (rules of thumb)

Page background =background(#F8F6)
Anything elevated (sneaker cards, inputs, sheets) =surface(#FFFF)outlineborno shadowb
Hero blocks (Drop of the week, sticky CTA bar, bottom nav) =primarybonPrimarytext and `secsecondarylime as the si
Lime (secondary) is **nevnever a large fill — only pills, dots, micro-tags, one accent word per hero
🔤 T
Font family:

Display : `Space GroSpace Grotesk(700) — headings, prices, hero tex
Body :Inter(400/500/600/700)
Accent :Serif Instrument(Regular Italic) — one accent word per hero ("sole", "S
Mast

Style	Make	Size / Line	Weight	Tracking	Used for
displayLarge	Space Grotesk	3	700	-0.	Home headline ("Find yo
headlineMedium	S	24	7	-0.4	Listing title, screen t
titleLarge	S	18	70	-0.	Section headers ("For you", "Order sum
titleMedium	I	15	60	0	Card titles (s
Large body	I	15/22 sp	40	0	Descriptions, paragraphs
bodyMedium	I	13	4	0	Cape
Large label	In	14/	600	0.1	Button
labelMedium	Int	1	60	0.	Ch
labelSmall	In	10	700	1.0	Tags ("DROP OF THE WEEK", "NEW"
price(	S	1	7	-0.6 sp, tabular nums	All money will
Hierarchy rules

Bold (700): screen titles, sneaker names on detail, prices, CT
Semibold (600): card titles, tab labels, br
Regular (400): description
onSurfaceVariantfor anything secondary; never gray out pri
One italic-serif accent word per hero. Never two.
📐 Spac
Scale (useDpconstants): `4 · 8 · 12 · 16 ·4 · 8 · 12 · 16 · 20 · 24 · 32 · 40

Context	Value
Screen horizontal paddin	20 dp
Screen top padding (under sta	28 dp
Secti	24 dp
C	16 dp (2
Gap between cards in a list	12 dp
Grid gutter (2-col sneaker grid)	12 dp horizontal · 24 dp vertical
Ga	16 dp
Gap bet	8 dp
Icon	8 dp
Bottom nav clearance (content padding-bottom)	96 dp
🧱
Buttons

Primary: height 52 dpcorner18 dp , `concontainerColor = primary,contentColor = onPrimary, `labelLarge label, ful
Secondary / outlined: height 52 dp, 18 dp radius, 1 dpoutlineedgesurfacefillonSurfacet
Icon button (top bar): 40 dp circle,surfaceVariantfill, 18 dp icon. Cart icon button usesprimaryfill + lime badge.
FAB-style sticky CTA bar: full width, 64 dp tall, 24 dp radius,primarybg, sits 12 dp above screen botto
Input fields (TextField)

Height 52 dp ,16 dp, `onsurfacebacoutlineedge
Focus: border becomesprimary1.5 dp
Internal padding: 16 dp horizontal
Label sits above the field (not floating) inlabelMedium, `onSuonSurfaceVariant.
Helper/error text below inbodyMedium(error inerrorcolor).
Cards

Sneaker tile: corner 20 dp ,surfacefill, 1 dpoutline, no elevation. Image area 1:1 withsurfaceVariantbackground, image inset 8 dp.
Info card (cart row, summary): corner 20 dp ,surface, 1 dpoutline16 dp padding.
Hero / dark card: corner 24 dp ,primaryfill, 20 dp padding, lime accent inside.
Chips / pills

Brand chip: height 36 dp , fully rounded (18 dp), 16 dp horizontal padding,labelMedium.
Inactive:surfacefill, 1 dpoutline,onSurfacetext.
Active:primaryfill,onPrimarytext. (Lime reserved for the single hero accent — keep brand chi
Size pill (listing detail): 56 × 44 dp, 14 dp radius,surface+ 1 dpoutlineSelected =primaryfill.
Tag pill ("NEW", "DROP OF THE WEEK"): height 22 dp, 11 dp radius,secondaryfill,onSecondarytext,labelSmall.
Top bar

64 dp tall, no elevation, transparent overbackground.
Left: avatar (40 dp circle) + 2-line greeting ("Welcome back" / first name).
Right: 40 dp notification button + 40 dp cart button with lime count badge.
Bottom navigation (custom, not stock M3)

Floating pill: 72 dp tall (incl. 16 dp bottom inset), 28 dp radius,primarybackground, 20 dp horizontal screen margin, sits 12 dp from bottom.
4 items: Shop / Sell / Cart / Me. Inactive = icon-only,onPrimaryat 55% alpha. Active = lime pill behind icon + label,onSecondarytext.
Active pill: rounded 22 dp,secondaryfill, icon scales to 1.1×.
1. LOGIN
Layout
28 dp top padding · 20 dp horizontal.
Centered SneakX wordmark (Space Grotesk 700, 28 sp) with one italic serif letter for accent.
HeadlineheadlineMedium"Welcome back."
SubheadLarge body onSurfaceVariant: "Sign in to keep trading on campus."
32 dp gap → Email field → 16 dp → Password field (with eye toggle).
8 dp gap → "Forgot password?" link, right-aligned,labelMedium onSurfaceVariant.
24 dp → Primary button "Sign in" (full width).
Bottom: "New here? Create an account " — link underlined,primary.
Implementation notes
Pure UI. WireonSignIn(email, password)to existing auth call.
No social login buttons unless backend supports them — omit if not.
2. REGISTER
Layout
Same shell as Login.
Headline: "Join SneakX."
Fields: Full name → Email → Password → Confirm password (16 dp gaps).
Helper under password: "8+ characters" inbodyMedium onSurfaceVariant.
Primary button "Create account" .
Bottom link: "Have an account? Sign in ".
Implementation notes
No phone, no avatar upload to register (keep student-app realistic).
Skip email verification UI unless backend sends a code.
3. MARKETPLACE (HOME)
Layout (top → bottom, all inside a vertical scroll)
Top bar (avatar + greeting · notification + cart).
Hero headline : "Find your sole ." (italic serif on "sole"), subhead "Authentic sneakers, traded on campus."
Search row : search field (flex 1, leading search icon, placeholder "Air Max, Jordan, Samba…") + 52 × 52 dpprimaryfilter button with sliders icon.
Featured banner (24 dp radius,primarybg):
Top-left lime tag pill "DROP OF THE WEEK" with pulsing dot.
Title (Space Grotesk 22 sp, 700) with one italic serif accent word in lime.
Price + lime "Shop ↗".
Right: product image, rotated 8°, with soft lime glow blur behind it.
Brand chips row : horizontally scrollable, 8 dp gap, no scrollbar. Chips: All · Nike · Adidas · Jordan · New Balance · Yeezy · Asics.
Section header : "For you" (titleLarge) + subtitle "{n} listings · curated" + right-aligned "Sort" link.
2-col sneaker grid , gutter 12 × 24 dp.
Sneaker card (reused everywhere)
20 dp radius,surface+ 1 dpoutline.
Top: 1:1 image area,surfaceVariantbg, inset image 8 dp. Top-left: condition tag pill ("NEW"/"USED"). Top-right: heart icon button (32 dp).
Body (12 dp padding): brandlabelMedium onSurfaceVariantUPPERCASE → nametitleMedium(1 line, ellipsis) → row: price (pricestyle) + small "↗" icon button.
Implementation notes
Brand chips, search, "Sort" — all UI-driven; wire to existing query params.
Wishlist heart: skip if no backend endpoint, keep purely visual.
Notification
4. LISTING DETAIL
Layout
Top bar : 40 dp csurfaceVariant.
Hero image : full-width, asurfaceVariantbg, 24onSurfaceVariant, active = `primaprimary).
Content (20 dp padding, scrolls under sticky CTA):
BrandlabelMediumUPPERCASE.
TitleheadlineMedium(e.g., "Air Max 97")Silver Bullet " — it
Row: priceprice22bodyMedium`oonSurfaceVariant.
"Select size"`titleLargtitleLargeGrid of size pprimaryfill.surfaceVariant+ diagonal strike.
24 dp → "Details" `titleLartitleLarge→ `bodyLaLarge bodyof
24 dp → small spec rows (Colorway · Condition · Posted) — label leftonSurfaceVariant, goonSurface`titletitleMedium.
Sticky bottom CTA bar : 64 dpprimarybg, sits oonPrimary) +"Add to cart"pill buttonSecondarytex
Implem
Seller name / rating / shipping ETA — omitted entirelyunless ba
"Posted" date only if backend hascreated_at; othe
Image carousel — if listings have only one image, render single image with
5.
Layout
Top bar: back button + title "New listing" center
20 dp pa
Photo upload row : horizontal, 4 slots (1 main 100 × 100 dp + 3 small 80 × 80 dp). Eacoutline, `surfaceVasurfaceVariantbg, centered "+" icon. Filled = image preview with small ✕ but
Section "Details" (titleLarge):
Title inpu
Brand input → presented as a dropdown chooser (opens bottom s
Colorway i
Section "Specs":
Size inp
Condition segmented: 2-cell row of pill buttons "New" / "Used", height 44 dp, full widprimary,surface+ tool
"Price" section:
Price input wi
Helper text below: "SneakX takes 0% — keep it all."bodyMedium`onSurfaonSurfaceVariant.
Section "Description":
Multiline TextField, min 4 lines, max 8 with internal s
24 dp gap → Primary button "Publish listing" full wid
Imp
Image picker — wire to existing upload pipel
Brand list — pull from your existing brand source; if none, use a free-text input instead of dropd
Drop the "0% fee" helper line if it isn't true
6. CA
Layou
T
If empty: centered illustration block (use a simple roundedsurfaceVariantsquaretitleLarge"Your cartLarge body`onSuronSurfaceVariantsubtit
If items:
Vertical list of cart row cards (12 dp gap):
20 dp
Layout: 80 × 80 dp image (16 dp radius,surfaceVariant) |labelMedium,titleMedium, "SbodyMedium`onSuonSurfaceVariant) | rightprice16
Qty stepper : pill 96 × 32 dp, `surfaceVarsurfaceVariant, "−"
Swipe-left or trailing "Remove" link (error,labelMedium).
Summary card at
20 dp radius,surface+ outline, 16 dp padding.
Rows (label leftonSurfaceVariant, value right): Subtotal · Service fee · Campus pickup → divider → **TotTotal (titleLargeLprice20 sp
Sticky CTA : full-width "Checkout · ${total}" primary button, same sticky pattern as listing detail.
Implementati
"Service fee" / "Campus pickup" — keep only if backend computes them, otherwise show only Subtotal + Total.
Qty stepper limits — clamp to backend's stock if available; else 1–10.
7. CHE
Lay
Top bar: b
Step indicator (optional, `llabelMedium):primaryA
"Pickup / Address" section (`tititleLarge):
Toggle (segmented 2-cell)
If pickup: dropdown of campus locations as a list of selectable cards (1 dp outline, 16 d
If shipping: stacked inputs — Full name · Address li
"Payment" section :
2-cell segmented: "Card" / "Cash on pickup".
Card mode: Card number · Expiry (50%) · CVC (50%) — inputs side-by-side with 1
Section "Order summary" : compact list (thumbnail 40 dp + name + qty + price) followed by the same Subtotal/T
Sticky CTA :"Place order · ${total}", primary.
Success state (after submit): full-screen overlay, centered: large lheadlineMedium"OrdLarge body"We'll ping you when your seller confirms
Imp
Only show the payment options your backend actually supports.
Skip address fields entirely if you only do campus pickup.
The success copy should match what your backend actually does (eg don't say "ping you" if there are no
8. P
Lay
Top b
Identity block (centered)
88 dp avatar circle, 2 dpsurfacer
headlineMediumname.
Large body`onSurfaceVaronSurfaceVariant"@handle ·
Stats row : 3surface+ outline, 16 dp padding. CentertitleLarge+ labellabelMedium`onSurfaceVariantonSurfaceVariant. Stats: Listed · Sold · Saved (drop "Earned" — too money-heavy for students
Tab switcher : 2 pills "My listings" / "PusurfaceVariantfill, active =surfacewith shadow none + 1 dp outline.
Thrilled :
Bottom: ghost "Log out" button,errortext, centered, 24
Im
"Saved" stat only if you have a wishlist table — else show 2 cards instead of 3.
"Purchases" tab: if order data has no images, fall back to a list-row layout (same as cart row) instead
No edit-profile screen unless backend supports it; gear icon can open a simple sheet with "Log out
9. ADMIN DASHBOARD
Mobile-first admin (since this is an Android app). Keep it a clean list UI , not a date

L
Top bar: title "Admin" + smallsecondarylime
KPI strip : horizontally scrollable cards, each 160 × 100 dp, 20 dpsurface+ outline, 16 dp
Card content: tiny labellabelMediumUPonSurfaceVariant(egheadlineMedium, delta libodyMedium(`succsuccessgolderror).
Cards: Users · Listings · Orders this week · Pending r
Section "Recent listings" (titleLarge):
List of rows (12 dp gap), each: 56 × 56 dp image · column (nametitleMediumbrand + pricebodyMedium`onSurfaceVarionSurfaceVariant) · right statusprimary/Pending=`susurfaceVariant/Derror10% bg +errort
Row tap → op
Section "Recent users" : l
Section "Reports" (only if backend has reports): same row pattern with reas
Implementati
Only render the KPI cards for metrics your backend actually returns.
Admin actions (ban, delete, approve) should be wrapped in confirmation dialogs (M3AlertDialog— keep themerror"Con
If your admin role can't act on something, show the row but hide the action
10. CROSS-SCRE
States : every list screen needs three states — loading (use M3Skeleton-style placeholdersurfaceVariant), empty (centerederrorcollar
Transitions :AnimatedContentTransitionScope— slide-horizontal between routes, fade for tab switches, scale 0.96→1combinedClickable+ `animateCoanimateContentSize).
Haptics : light tick on chip select, size pill select, and "Add to cart" tap
Dark mode : invert background↔ink, keep lime accent identical, raisesurfacet#1F1F25. Defe
11. WHAT TO IGNORE
Element	Drop if backend doesn't have…
Seller	seller relationship
Ratings / reviews anywhere	ratings table — already excluded
Shipping address	shipping supplement
Notification be	not
W	wishlist endpoi
"Posted"	created_at
"E	payout tracking
Reports	reports tab
Delivery tra	— already excluded
That's the full spec — colors, type, spacing, components, and per-screen layout — mapped 1:1 onto Material 3 so it drops straight into Compose.
