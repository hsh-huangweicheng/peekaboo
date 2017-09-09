const _ = require('underscore');
const fs = require('fs');

const wos = `acoustics
agricultural economics policy
agricultural engineering
agriculture dairy animal science
agriculture multidisciplinary
agronomy
allergy
anatomy morphology
anesthesiology
anthropology
archaeology
architecture
area studies
art
astronomy astrophysics
automation control systems
behavioral sciences
biochemical research methods
biochemistry molecular biology
biodiversity conservation
biology
biophysics
business
cardiac cardiovascular systems
cell tissue engineering
chemistry analytical
chemistry applied
chemistry inorganic nuclear
chemistry medicinal
chemistry multidisciplinary
chemistry organic
chemistry physical
clinical neurology
communication
computer science information systems
computer science interdisciplinary applications
computer science software engineering
computer science theory methods
construction building technology
criminology penology
critical care medicine
dermatology
ecology
economics
education scientific disciplines
electrochemistry
emergency medicine
endocrinology metabolism
energy fuels
engineering aerospace
engineering biomedical
engineering chemical
engineering civil
engineering electrical electronic
engineering environmental
engineering industrial
engineering manufacturing
engineering mechanical
engineering multidisciplinary
entomology
environmental sciences
environmental studies
ethics
family studies
fisheries
food science technology
forestry
gastroenterology hepatology
genetics heredity
geochemistry geophysics
geography
geology
geosciences multidisciplinary
geriatrics gerontology
gerontology
green sustainable science technology
health care sciences services
health policy services
hematology
history
horticulture
hospitality leisure sport tourism
imaging science photographic technology
immunology
industrial relations labor
infectious diseases
information science library science
instruments instrumentation
international relations
limnology
linguistics
logic
management
materials science biomaterials
materials science ceramics
materials science characterization testing
materials science coatings films
materials science composites
materials science multidisciplinary
mathematics
mechanics
medical informatics
medical laboratory technology
medicine general internal
medicine research experimental
metallurgy metallurgical engineering
meteorology atmospheric sciences
mineralogy
multidisciplinary sciences
mycology
nanoscience nanotechnology
neuroimaging
neurosciences
nuclear science technology
nursing
nutrition dietetics
oceanography
oncology
ophthalmology
optics
ornithology
paleontology
parasitology
pathology
pediatrics
peripheral vascular disease
pharmacology pharmacy
physics applied
physics atomic molecular chemical
physics condensed matter
physics fluids plasmas
physics mathematical
physics multidisciplinary
physics nuclear
physiology
planning development
plant sciences
political science
polymer science
psychiatry
psychology
public administration
public environmental occupational health
radiology nuclear medicine medical imaging
remote sensing
respiratory system
rheumatology
social issues
social sciences biomedical
social sciences interdisciplinary
social sciences mathematical methods
social work
sociology
soil science
spectroscopy
sport sciences
statistics probability
substance abuse
surgery
thermodynamics
toxicology
transplantation
transportation
tropical medicine
urban studies
urology nephrology
veterinary sciences
virology
water resources
women's studies
zoology`;

const esi = `materials sciences
geosciences
multidisciplinary
molecular biology & genetics
engineering
chemistry
environment/ecology
computer science
psychology/psychiatry
economics & business
space science
clinical medicine
immunology
agricultural sciences
neuroscience & behavior
biology & biochemistry
mathematics
microbiology
physics
pharmacology & toxicology
social sciences, general
plant & animal science`;

const wosList = wos.split('\n');
const esiLines = esi.split('\n');
const list = wosList.map((wosLine) => {
    const reg = /[\W]+/;
    const esiList = esiLines.filter((esiLine) => {
        return _.intersection(wosLine.split(reg), esiLine.split(reg)).length;
    });

    return [wosLine, ...esiList].join('\t');
});

fs.writeFileSync('out.txt',list.join('\r\n'));